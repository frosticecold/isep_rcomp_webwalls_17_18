/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import application.Settings;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raúl Correia
 */
public class UDPServer implements Runnable {

    private static DatagramSocket socket;

    public UDPServer() {
        try {
            socket = new DatagramSocket(Settings.UDP_PORT);
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
        while (true) {
            try {
                /**
                 * receive answer *
                 */
                udpPacket.setData(buffer);
                udpPacket.setLength(buffer.length);
                socket.receive(udpPacket);
                
                /**
                 * Interpretar
                 */
                /**
                 * get answer length *
                 */
                packet_length = udpPacket.getLength();
                System.out.println("Request from: " + udpPacket.getAddress().getHostAddress()
                        + " port: " + udpPacket.getPort());
                String s = "Server ip: " + socket.getLocalAddress().getHostAddress() + " " + new String(buffer);
                /**
                 * send answer *
                 */
                boolean finished = false;
                do{
                   
                }while(!finished);
                udpPacket.setData(s.getBytes());
                udpPacket.setLength(s.getBytes().length);
                socket.send(udpPacket);
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
