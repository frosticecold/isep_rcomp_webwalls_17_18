package server.http;

import application.Settings;
import server.domain.Wall;
import server.domain.WallManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Santos <1161386@isep.ipp.pt>
 */
public class HTTPServer extends Thread {

    static private final String BASE_FOLDER = "www";
    static private ServerSocket sock;
    private static volatile boolean running = true;

    // DATA ACCESSED BY THREADS - LOCKING REQUIRED
    /**
     * Method to find or create a wall 
     * 
     * @param wallname Wallname
     * @return the wall created
     */
    public static synchronized Wall getWallStandingInHTML(String wallname) {
        Wall wall = WallManager.getInstance().findOrCreateWall(wallname);
        return wall;
    }

    @Override
    public void run() {
        try {
            Socket cliSock;
            sock = new ServerSocket(Settings.TCP_PORT);
            while (running) {
                cliSock = sock.accept();
                printRequest(cliSock);
                HTTPRequest req = new HTTPRequest(cliSock, BASE_FOLDER);
                req.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(HTTPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printRequest(Socket clientsocket) {
        System.out.printf("HTTP Request from: %s:%d\n", clientsocket.getInetAddress().getHostAddress(), clientsocket.getPort());
    }

    public void exit() {
        running = false;
    }
}
