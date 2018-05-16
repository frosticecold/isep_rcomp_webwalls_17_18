/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import server.udp.Protocol;
import application.Settings;
import client.gui.GUIClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class UDPClient implements Runnable {

    private static InetAddress serverIP;
    private static volatile boolean execute = true;
    private static DatagramSocket sock;

    private Semaphore semSocket = new Semaphore(1, true);
    private static final int SOCKET_TIMEOUT = 2000;

    private static int expectedSize = 0;
    private static int currentByteCount = 0;

    private static String content = "";

    private static final int BUFFER_SIZE = 300;

    /**
     * Creates a new UDPClient based on the ip
     *
     * @param ip IP
     */
    public UDPClient(String ip) {
        try {
            serverIP = InetAddress.getByName(ip);
            sock = new DatagramSocket();
            //sock.setSoTimeout(SOCKET_TIMEOUT);
        } catch (UnknownHostException ex) {
            System.out.println("Invalid server address supplied: " + ip);
            System.exit(1);
        } catch (SocketException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            byte[] data = new byte[BUFFER_SIZE];
            String frase;
            DatagramPacket udpPacket = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);
            System.out.println("Getting information...");
            while (execute) {
                sock.receive(udpPacket);
                //int udp_length = udpPacket.getLength();
                frase = new String(udpPacket.getData(), 0, udpPacket.getLength());
                System.out.println("Received reply: " + frase);

                /**
                 * Interpretar resposta
                 */
                Interpreter.resolve(udpPacket);

            }
            sock.close();
        } catch (SocketTimeoutException ex) {
            /*
            Do nothing
             */
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Exit...");
    }

    /**
     * Returns the server ip
     *
     * @return the server ip as string
     */
    public String getServerIP() {
        return serverIP.getHostAddress();
    }

    /**
     * Exit
     */
    public void exit() {
        execute = false;
    }

    /**
     * Message to be sent by the UDPClient to the server
     *
     * @param message the message
     */
    public void sendMessage(final String message) {
        if (message != null && !message.isEmpty()) {
            try {
                byte[] data = message.getBytes();
                /*
                        Send message
                 */
                DatagramPacket udp = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);
                sock.send(udp);
                System.out.println("Message: " + message);
                System.out.println("Sent content to: " + serverIP.getHostAddress() + ":" + Settings.UDP_PORT);
            } catch (IOException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     * @param wallname Wall Name
     * @param message Message
     */
    public void sendWallMessageWithHeader(final String wallname, final String message) {
        if (message != null && !message.isEmpty()) {
            try {
                String msg = Protocol.buildWallAndMessageCommand(wallname, message);
                byte[] data = msg.getBytes();
                /*
                        Send message
                 */
                DatagramPacket udp = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);
                sock.send(udp);
                System.out.println("Message: " + message);
                System.out.println("Sent content to: " + serverIP.getHostAddress() + ":" + Settings.UDP_PORT);
            } catch (IOException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     */
    public void sendHello() {
        sendMessage("@hello");
    }

    /**
     *
     */
    public static void resetValues() {
        expectedSize = 0;
        currentByteCount = 0;
        content = "";
    }

    static class Interpreter {

        static void resolve(DatagramPacket packet) {
            String line = new String(packet.getData(), 0, packet.getLength());
            if (!line.isEmpty()) {
                String[] args = line.split(Protocol.MSG_SPLITTER);
                if (resolveCommands(args) == false) {
                    resolveContent(args);
                }
            }
        }

        static boolean resolveCommands(String[] args) {
            boolean iscommand = false;
            if (args[0].charAt(0) == Protocol.STARTING_COMMAND) {
                String command = args[0];
                switch (command) {
                    //Hello
                    case "@hello":
                        GUIClient.getInstance().enableChat();
                        System.out.println("Enabling chat...");
                        iscommand = true;
                        resetValues();
                        break;
                    case "@checksum_server":
                        if (currentByteCount == expectedSize) {
                            GUIClient.getInstance().changeWallText(content);
                            System.out.println("Successfully received wall content.");
                        } else {
                            final String currentWall = GUIClient.getInstance().getCurrentWallName();
                            String resend_command = Protocol.buildGetWallCommand(currentWall);
                            client.Client.getInstance().sendMessage(resend_command);

                            resetValues();
                        }
                        iscommand = true;
                        break;
                    case "@success":
                        GUIClient.getInstance().showMessageSuccess(true);
                        break;
                    case "@failed":
                        GUIClient.getInstance().showMessageSuccess(false);
                        break;
                    case "@error@resend@message":
                        final String message = GUIClient.getInstance().getMessage();
                        final String wallname = args[1];
                        client.Client.getInstance().sendMessageToWall(wallname, message);
                        break;
                    default:
                        break;
                }
            }
            return iscommand;
        }

        static void resolveContent(String[] args) {
            /**
             * Verificar se vamos receber o total de bytes a receber
             * <p>
             * Tag especial #!
             */
            /*if (args[0].contains(Protocol.MSG_TOTAL_CONTENT)) {
                String msg = args[0].replace(Protocol.MSG_TOTAL_CONTENT, "");
                expectedSize = Integer.parseInt(msg);
                contentlist.clear();

            } else {
                /**
                 * Senão é porque é conteúdo e adicionar à lista
             */

            if (args[0].equals(Protocol.MSG_TOTAL_CONTENT)) {
                expectedSize = Integer.parseInt(args[1]);
            } else {
                content += args[0];
                currentByteCount += args[0].getBytes().length;
            }

        }

        static LinkedList<DatagramPacket> buildMultipleMessage(InetAddress serveripaddress, int port, byte[] content) {

            LinkedList<DatagramPacket> ll_packets = new LinkedList<>();
            int indexContent = 0;
            int indexPacket = 1;

            final int UDP_BODY_SIZE = Settings.UDP_PACKET_SIZE - Settings.UDP_PACKET_HEADER;

            boolean finished = false;
            LinkedList<byte[]> splitWallInformation = splitInformation(content, UDP_BODY_SIZE);
            while (!splitWallInformation.isEmpty()) {
                //byte header[] = buildPacketHeader(indexPacket);
                byte data[] = splitWallInformation.pop();
                DatagramPacket packet = new DatagramPacket(data, data.length, serveripaddress, port);
                ll_packets.add(packet);
            }
            return ll_packets;
        }

        private static DatagramPacket buildChecksumPacket(InetAddress serveripaddress, int port, byte[] wallcontent) {

            String str = Protocol.buildTotalChecksumPacket(wallcontent);
            byte[] data = str.getBytes();
            return new DatagramPacket(data, data.length, serveripaddress, port);
        }

        private static LinkedList<byte[]> splitInformation(final byte[] messagecontent, final int UDP_BODY_SIZE) {
            LinkedList<byte[]> content = new LinkedList<>();
            byte[] data = messagecontent;
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

        private static void sendMultiplePackets(InetAddress serveripaddress, int port, byte[] wallcontent, LinkedList<DatagramPacket> packets) throws IOException {
            /**
             * Send how much information we going to send
             */
            DatagramPacket checksumpacket = buildChecksumPacket(serveripaddress, port, wallcontent);
            sock.send(checksumpacket);
            while (!packets.isEmpty()) {
                DatagramPacket packet = packets.pop();
                sock.send(packet);
            }
            String checksum = Protocol.KEYWORDS[Protocol.CHECKSUM_COMMAND];
            byte data[] = checksum.getBytes();
            checksumpacket = new DatagramPacket(data, data.length, serveripaddress, port);
            sock.send(checksumpacket);
        }
    }
}
