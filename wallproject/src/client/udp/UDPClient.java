/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.udp;

import UDP.Protocol;
import application.Settings;
import client.gui.GUIClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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

    private static List<String> contentlist = new ArrayList<>();

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
            byte[] data = new byte[300];
            String frase;
            DatagramPacket udpPacket = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);
            System.out.println("Getting information...");
            while (execute) {
                sock.receive(udpPacket);
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

    public String getServerIP() {
        return serverIP.getHostAddress();
    }

    public void exit() {
        execute = false;
    }

    public void sendMessage(final String message) {
        if (message != null) {
            try {
                byte[] data = message.getBytes();
                DatagramPacket udp = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);
                sock.send(udp);
                System.out.println("Sent message to: " + serverIP.getHostAddress());
            } catch (IOException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void sendHello() {
        try {
            byte[] data = "@hello".getBytes();
            DatagramPacket udp = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);
            sock.send(udp);
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class Interpreter {

        static void resolve(DatagramPacket packet) {
            byte[] data = packet.getData();
            String line = new String(data).trim();
            if (!line.isEmpty()) {
                String[] args = line.split(Protocol.MSG_SPLITTER);
                resolveCommands(args);
                resolveContent(args);
            }
        }

        static void resolveCommands(String[] args) {
            if (args[0].charAt(0) == Protocol.STARTING_COMMAND) {
                String command = args[0];
                switch (command) {
                    //Hello
                    case "@hello":
                        GUIClient.getInstance().enableChat();
                        System.out.println("Enabling chat...");
                        break;

                }
            }
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

            if (args[0].charAt(0) == Protocol.MSG_INDEX) {
                contentlist.add(args[1]);
                currentByteCount = args[1].getBytes().length;
            }

        }

    }
}
