/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import client.gui.GUIClient;
import HTTP.HTTPServer;
import UDP.UDPServer;
import client.Client;
import domain.WallManager;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Raúl Correia
 */
public class Application {

    public static volatile boolean running = true;
    public static Settings settings;
    public static HTTPServer http_server;
    public static Thread http_thread;
    public static UDPServer udp_server;
    public static Thread udp_thread;

    /**
     * Main method with a test wall named TestWall with a message.
     *
     * @param args server or client instance
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: no parameters.");
        }

        WallManager.getInstance().findOrCreateWall("TestWall");
        WallManager.getInstance().addMessageToWall("TestWall", "This is a test message");
        try {
            settings = new Settings();
        } catch (IOException ex) {
            System.out.println("Error: couldn't load settings");
        }
        decide(args);
        Scanner scan = new Scanner(System.in);
        while (running) {
            System.out.printf("Please input a command<exit>>");
            String command = scan.nextLine();
            if (command.matches("exit")) {
                exit();
            }
        }
    }

    /**
     * Decide if is running a server or client
     * 
     * @param args server or client
     */
    private static void decide(String[] args) {

        if (args.length == 0) {
            System.out.println("Error: no arguments");
            System.exit(1);
        }
        if (args.length > 0) {
            if (args[0].compareToIgnoreCase("server") == 0) {
                http_server = new HTTPServer();
                http_thread = new Thread(http_server);
                udp_server = new UDPServer();
                udp_thread = new Thread(udp_server);
                System.out.println("Running UDPServer thread on port: " + Settings.UDP_PORT);
                System.out.println("Running HTTPServer thread on port: " + Settings.TCP_PORT);
                http_thread.start();
                udp_thread.start();
            } else {
                if (args.length >= 1) {
                    if (args[0].compareToIgnoreCase("client") == 0) {
                        GUIClient gui = new GUIClient();
                        System.out.println("Running UDPClient thread on server IP: ");
                    }
                } else {
                    System.exit(1);
                }
            }
        }
    }

    /**
     *
     */
    public static void exit() {
        running = false;
        udp_server.exit();
        http_server.exit();
        Client.udp_client.exit();
        System.exit(0);
    }

}
