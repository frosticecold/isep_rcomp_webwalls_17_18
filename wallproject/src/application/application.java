/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.IOException;

/**
 *
 * @author Raúl Correia
 */
public class application {

    public static Settings set;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: no parameters.");
        }

        try {
            set = new Settings();
        } catch (IOException ex) {
            System.out.println("Error: couldn't load settings");
        }

    }

}
