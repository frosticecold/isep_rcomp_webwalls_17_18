/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import server.udp.Protocol;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class Client {

    /**
     * UDP Client
     */
    public static UDPClient udp_client;

    /**
     * UDP Thread
     */
    public static Thread udp_thread;

    private static Client instance;

    private Client() {
    }

    /**
     * Instance of Client
     *
     * @return Client
     */
    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    /**
     * Provides a new connection to the UDP Thread
     *
     * @param ip IP
     */
    public void newConnection(final String ip) {
        if (udp_thread != null) {
            udp_thread.interrupt();
        }
        udp_client = new UDPClient(ip);
        udp_thread = new Thread(udp_client);
        udp_thread.start();
    }

    /**
     * Sends a message
     *
     * @param message Message
     */
    public void sendMessage(final String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if (message.charAt(0) == Protocol.STARTING_COMMAND) {
            udp_client.sendMessage(message);
        }

    }

    /**
     * Sends a message to a wall
     *
     * @param wallname Wall
     * @param message Message
     */
    public void sendMessageToWall(final String wallname, final String message) {
        if (message == null || message.isEmpty()) {
            return;
        }

        udp_client.sendWallMessageWithHeader(wallname, message);
    }

    /**
     * Functionality test @Hello
     *
     */
    public void sendHello() {
        udp_client.sendHello();
    }

    /**
     * Sends @getwall;wallname message
     *
     * @param wallname Wall Name
     */
    public void sendGetWall(final String wallname) {
        udp_client.sendMessage(Protocol.buildGetWallCommand(wallname));
    }

    /**
     * Exit
     */
    public void exit() {
        udp_client.exit();
    }
}
