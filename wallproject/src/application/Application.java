/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import UDP.UDPClient;
import UDP.UDPServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raúl Correia
 */
public class Application {
    
    public static Settings settings;
    public static UDPServer udp_server;
    public static UDPClient udp_client;
    public static Thread udp_thread;
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: no parameters.");
        }
        
        try {
            settings = new Settings();
        } catch (IOException ex) {
            System.out.println("Error: couldn't load settings");
        }
        decide(args);
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void decide(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: no arguments");
            System.exit(1);
        }
        if (args.length > 0) {
            if (args[0].compareToIgnoreCase("server") == 0) {
                udp_server = new UDPServer();
                udp_thread = new Thread(udp_server);
                System.out.println("Running UDPServer thread on port: " + Settings.UDP_PORT);
                udp_thread.start();
            } else {
                if (args.length > 1) {
                    if (args[0].compareToIgnoreCase("client") == 0) {
                        udp_client = new UDPClient(args[1]);
                        udp_thread = new Thread(udp_client);
                        System.out.println("Running UDPClient thread on server IP: " + udp_client.getServerIP() + " on port: " + Settings.UDP_PORT);
                        udp_thread.start();
                    }
                } else {
                    System.exit(1);
                    
                }
            }
        }
    }
    
}
