/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import client.gui.GUIClient;
import server.http.HTTPServer;
import server.udp.UDPServer;
import client.Client;
import java.awt.Desktop;
import server.domain.WallManager;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raúl Correia
 */
public class Application {

    public static volatile boolean running = true;
    public static Settings settings;
    public static HTTPServer http_server;
    public static UDPServer udp_server;

    /**
     * Main method with a test wall named TestWall with a message.
     *
     * @param args server or client instance
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: arguments: server client");
            System.exit(1);
        }

        WallManager.getInstance().findOrCreateWall("TestWall");
        WallManager.getInstance().addMessageToWall("TestWall", "This is a test message");
        try {
            settings = new Settings();
        } catch (IOException ex) {
            System.out.println("Error: couldn't load settings");
            System.exit(1);
        }
        decide(args);
        Scanner scan = new Scanner(System.in);
        Timer timer = new Timer("Timer thread");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                long memInMB = (long) (mem * Math.pow(10, -6));
                System.out.printf("Memory Usage: %d MB\n", memInMB);
            }
        },0, 15000);
        while (running) {
            System.out.printf("Please input a command<exit>\n"
                    + "$>");
            String command = scan.nextLine();
            if (command.matches("exit")) {
                try {
                    exit();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                }
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
            System.out.println("Error: use as arugments server client");
            System.exit(1);
        }
        if (args.length > 0) {
            if (args[0].compareToIgnoreCase("server") == 0) {
                http_server = new HTTPServer();
                udp_server = new UDPServer();
                System.out.println("Running UDPServer thread on port: " + Settings.UDP_PORT);
                System.out.println("Running HTTPServer thread on port: " + Settings.TCP_PORT);
                http_server.start();
                udp_server.start();
                if(args.length > 1) {
                    if (args[1].compareToIgnoreCase("browser") == 0) {
                        openHomePage();
                    }
                }
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
     * @throws java.lang.InterruptedException
     */
    public static void exit() throws InterruptedException {
        running = false;
        System.exit(0);
    }
    
    private static void openHomePage() {
        try {
            URI homepage = new URI("http://localhost:" + Settings.TCP_PORT + "/");
            Desktop.getDesktop().browse(homepage);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}
