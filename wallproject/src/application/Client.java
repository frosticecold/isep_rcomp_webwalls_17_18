/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import UDP.UDPClient;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class Client {

    public static UDPClient udp_client;
    public static Thread udp_thread;
    
    
    public Client(){
    }

    public void newConnection(final String ip){
            udp_client = new UDPClient(ip);
            udp_thread = new Thread(udp_client);
            udp_thread.run();
    }
    
    public void sendMessage(final String message){
        
    }
    
    public void exit(){
        udp_client.exit();
    }
}
