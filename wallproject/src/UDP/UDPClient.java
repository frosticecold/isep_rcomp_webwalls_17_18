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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class UDPClient implements Runnable {

    private static InetAddress serverIP;

    public UDPClient(String ip) {
        try {
            serverIP = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            System.out.println("Invalid server address supplied: " + ip);
            System.exit(1);
        }

    }

    @Override
    public void run() {
        try {
            byte[] data = new byte[300];
            String frase;
            DatagramSocket sock = new DatagramSocket();
            DatagramPacket udpPacket = new DatagramPacket(data, data.length, serverIP, Settings.UDP_PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Sentence to send (\"exit\" to quit): ");
                frase = in.readLine();
                if (frase.compareTo("exit") == 0) {
                    break;
                }
                udpPacket.setData(frase.getBytes());
                udpPacket.setLength(frase.length());
                sock.send(udpPacket);
                udpPacket.setData(data);
                udpPacket.setLength(data.length);
                sock.receive(udpPacket);
                frase = new String(udpPacket.getData(), 0, udpPacket.getLength());
                System.out.println("Received reply: " + frase);
            }
            sock.close();
        } catch (SocketException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getServerIP() {
        return serverIP.getHostAddress();
    }
}
