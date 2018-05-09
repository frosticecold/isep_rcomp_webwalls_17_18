/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import UDP.Protocol;
import client.udp.UDPClient;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class Client {

    public static UDPClient udp_client;
    public static Thread udp_thread;

    private static Client instance;

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void newConnection(final String ip) {
        udp_client = new UDPClient(ip);
        udp_thread = new Thread(udp_client);
        udp_thread.start();
    }

    public void sendMessage(final String message) {
        udp_client.sendMessage(message);
    }

    public void sendHello() {
        udp_client.sendHello();
    }

    public void sendGetWall(final String wallname) {
        udp_client.sendMessage(Protocol.buildGetWallCommand(wallname));
    }

    public void exit() {
        udp_client.exit();
    }
}
