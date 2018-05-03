/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.net.DatagramPacket;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class Protocol {

    public static final String KEYWORDS[] = {"hello", "getwall", "confirm", "error;resend;", "success"};
    public static final String NUMBER_SPLITTER = "/";
    public static final String MSG_SPLITTER = ";";
    
    public Protocol() {
    }

    public DatagramPacket[] interpret(DatagramPacket source) {

        return null;
    }
}
