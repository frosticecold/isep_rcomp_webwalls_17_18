/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import application.Settings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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

    public UDPClient(String ip) {
        try {
            serverIP = InetAddress.getByName(ip);
            sock = new DatagramSocket();
            sock.setSoTimeout(SOCKET_TIMEOUT);
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
            while (execute) {
                System.out.println("Getting information...");
                sock.receive(udpPacket);
                frase = new String(udpPacket.getData(), 0, udpPacket.getLength());
                System.out.println("Received reply: " + frase);
            }
            sock.close();
        } catch (SocketTimeoutException ex) {
            /*
            Do nothing
             */
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
