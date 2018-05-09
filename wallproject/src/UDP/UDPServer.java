/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import application.Settings;
import domain.WallManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raúl Correia
 */
public class UDPServer implements Runnable {
    
    private static DatagramSocket sock;
    
    private static volatile boolean execute = true;
    
    public UDPServer() {
        try {
            sock = new DatagramSocket(Settings.UDP_PORT);
        } catch (SocketException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
    }
    
    @Override
    public void run() {
        System.out.println("Listening for UDP requests (IPv6/IPv4). Use CTRL+C to terminate the server.");
        byte[] buffer = new byte[Settings.UDP_PACKET_SIZE];
        byte[] response = new byte[Settings.UDP_PACKET_SIZE];
        int packet_length;
        /**
         * Buffer *
         */
        DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
        while (execute) {
            try {
                /**
                 * receive answer *
                 */
                udpPacket.setData(buffer);
                udpPacket.setLength(buffer.length);
                sock.receive(udpPacket);

                /**
                 * Interpretar
                 */
                Interpreter.resolve(udpPacket);
                /**
                 * get answer length *
                 */
                System.out.println("UDP Request from: "
                        + udpPacket.getAddress().getHostAddress() + " port: "
                        + udpPacket.getPort());
                /**
                 * packet_length = udpPacket.getLength();
                 *
                 *
                 * String ans = new String(udpPacket.getData());
                 * System.out.println("Message from: " +
                 * udpPacket.getAddress().getHostAddress() + " " + ans);
                 * udpPacket.setData(ans.getBytes());
                 * udpPacket.setLength(ans.getBytes().length);
                 * sock.send(udpPacket);
                 *
                 *
                 *
                 */
                buffer = new byte[Settings.UDP_PACKET_SIZE];
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    public void exit() {
        execute = false;
    }
    
    static class Interpreter {
        
        static void resolve(DatagramPacket request) {
            byte[] data = request.getData();
            String line = new String(data).trim();
            if (!line.isEmpty()) {
                try {
                    String[] args = line.split(Protocol.MSG_SPLITTER);
                    resolveCommands(request, args);
                } catch (IOException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        static void resolveCommands(DatagramPacket request, String[] args) throws IOException {
            if (args[0].charAt(0) == Protocol.STARTING_COMMAND) {
                String command = args[0];
                switch (command) {
                    //Hello
                    case "@hello": {
                        byte[] data = "@hello".getBytes();
                        DatagramPacket udp = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                        sock.send(udp);
                    }
                    break;
                    
                    case "@getwall": {
                        if (args.length > 1) {
                            if (!args[1].isEmpty()) {
                                //String strwall = WallManager.getInstance().getWallInformation(args[1]);
                                byte[] wallcontent = WallManager.getInstance().getWallInformationBytes(args[1]);
                                LinkedList<DatagramPacket> datagrampackets = buildMultipleMessage(request, wallcontent);
                                System.out.printf("Sending %d messages\n", datagrampackets.size());
                                sendMultiplePackets(request, wallcontent, datagrampackets);
                            }
                        }
                    }
                    break;
                    
                }
            }
        }
    }
    
    static LinkedList<DatagramPacket> buildMultipleMessage(DatagramPacket request, byte[] content) {
        
        LinkedList<DatagramPacket> ll_packets = new LinkedList<>();
        int indexContent = 0;
        int indexPacket = 1;
        
        final int UDP_BODY_SIZE = Settings.UDP_PACKET_SIZE - Settings.UDP_PACKET_HEADER;
        
        boolean finished = false;
        LinkedList<byte[]> splitWallInformation = splitWallInformation(content, UDP_BODY_SIZE);
        while (!splitWallInformation.isEmpty()) {
            //byte header[] = buildPacketHeader(indexPacket);
            byte data[] = splitWallInformation.pop();
            DatagramPacket packet = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
            ll_packets.add(packet);
        }
        return ll_packets;
    }
    
    private static DatagramPacket buildChecksumPacket(DatagramPacket request, byte[] wallcontent) {
        
        String str = Protocol.buildTotalChecksumPacket(wallcontent);
        byte[] data = str.getBytes();
        return new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
        
    }

    /*
    private static byte[] buildPacketHeader(int indexPacket) {
        byte header[] = new byte[Settings.UDP_PACKET_HEADER];
        for (int i = 0; i < header.length; i++) {
            if (i == 0) {
                header[i] = (byte) '#';
            } else {
                header[i] = (byte) '?';
            }
        }
        byte[] number = Integer.toString(indexPacket).getBytes();
        for (int i = 0; i < number.length; i++) {
            header[i + 1] = number[i];
        }
        return header;

    }

     */
    private static LinkedList<byte[]> splitWallInformation(final byte[] wallcontent, final int UDP_BODY_SIZE) {
        LinkedList<byte[]> content = new LinkedList<>();
        byte[] data = wallcontent;
        int numberOfPackets = (int) Math.ceil((double) data.length / UDP_BODY_SIZE);
        boolean running = true;
        int startingIndex = 0;
        int endIndex = UDP_BODY_SIZE - 1;
        do {
            if (data.length > UDP_BODY_SIZE) {
                byte[] udp_packet = Arrays.copyOfRange(data, startingIndex, endIndex);
                data = Arrays.copyOfRange(data, endIndex, data.length);
                content.add(udp_packet);
            } else {
                content.add(data);
                running = false;
            }
        } while (running);
        return content;
    }
    
    private static void sendMultiplePackets(DatagramPacket request, byte[] wallcontent, LinkedList<DatagramPacket> packets) throws IOException {
        /**
         * Send how much information we going to send
         */
        DatagramPacket checksumpacket = buildChecksumPacket(request, wallcontent);
        sock.send(checksumpacket);
        while (!packets.isEmpty()) {
            DatagramPacket packet = packets.pop();
            sock.send(packet);
        }
        String checksum = Protocol.getCommand(Protocol.CHECKSUM_SERVER_COMMAND);
        byte data[] = checksum.getBytes();
        checksumpacket = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
        sock.send(checksumpacket);
    }
}
