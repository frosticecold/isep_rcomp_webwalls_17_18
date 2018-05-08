/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HTTP;

import application.Settings;
import domain.Wall;
import domain.WallManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Santos <1161386@isep.ipp.pt>
 */
public class HTTPServer implements Runnable {

    static private final String BASE_FOLDER = "www";
    static private ServerSocket sock;

    public static void main(String args[]) throws Exception {
        Socket cliSock;

        if (args.length != 1) {
            System.out.println("Local port number required at the command line.");
            System.exit(1);
        }

        accessesCounter = 0;
        for (int i = 0; i < candidatesNumber; i++) {
            candidateName[i] = "Candidate " + i;
            candidateVotes[i] = 0;
        }

        try {
            sock = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException ex) {
            System.out.println("Server failed to open local port " + args[0]);
            System.exit(1);
        }
        while (true) {
            cliSock = sock.accept();
            HTTPRequest req = new HTTPRequest(cliSock, BASE_FOLDER);
            req.start();
            incAccessesCounter();
        }
    }

    // DATA ACCESSED BY THREADS - LOCKING REQUIRED
    private static LinkedList<Wall> wallList;
    private static final int candidatesNumber = 4;
    private static final String[] candidateName = new String[candidatesNumber];
    private static final int[] candidateVotes = new int[candidatesNumber];
    private static int accessesCounter;

    private static synchronized void incAccessesCounter() {
        accessesCounter++;
    }

    public static synchronized String getVotesStandingInHTML() {
        String textHtml = "<hr><ul>";
        for (int i = 0; i < candidatesNumber; i++) {
            textHtml = textHtml + "<li><button type=button onclick=voteFor(" + (i + 1)
                    + ")>Vote for " + candidateName[i] + "</button> "
                    + " - " + candidateVotes[i] + " votes </li>";
        }
        textHtml = textHtml + "</ul><hr><p>HTTP server accesses counter: " + accessesCounter + "</p><hr>";
        return textHtml;
    }

    public static synchronized Wall getWallStandingInHTML(String wallname) {
        Wall wall = WallManager.getInstance().findOrCreateWall(wallname);
        return wall;
    }
    
    public static synchronized void castVote(String i) {
        int cN;
        try {
            cN = Integer.parseInt(i);
        } catch (NumberFormatException ne) {
            return;
        }
        cN--;
        if (cN >= 0 && cN < candidatesNumber) {
            candidateVotes[cN]++;
        }
    }

    @Override
    public void run() {
        try {
            Socket cliSock;
            
            accessesCounter = 0;
            for (int i = 0; i < candidatesNumber; i++) {
                candidateName[i] = "Candidate " + i;
                candidateVotes[i] = 0;
            }
            
            sock = new ServerSocket(Settings.TCP_PORT);
            while (true) {
                cliSock = sock.accept();
                HTTPRequest req = new HTTPRequest(cliSock, BASE_FOLDER);
                req.start();
                incAccessesCounter();
            }
        } catch (IOException ex) {
            Logger.getLogger(HTTPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
