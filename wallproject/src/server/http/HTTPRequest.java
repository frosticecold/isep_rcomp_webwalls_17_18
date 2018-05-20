/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.http;

import server.domain.Wall;
import server.domain.WallManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Miguel Santos 1161386@isep.ipp.pt
 */
public class HTTPRequest extends Thread {

    String baseFolder;
    Socket sock;
    DataInputStream inS;
    DataOutputStream outS;

public HTTPRequest(Socket s, String f) {
        baseFolder = f;
        sock = s;
    }
    
    /**
     * Method to obtain the wall
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void methodGet(HTTPmessage request, HTTPmessage response) throws IOException {

        if (request.getURI().startsWith("/walls/")) {
            String uri[] = request.getURI().split("/");
            if (uri.length >= 2) {
                Wall wall = WallManager.getInstance().findOrCreateWall(uri[2]);
                response.setContentFromWall(wall, "text/html");
                response.setResponseStatus("200 Ok");
            }
        } else if (request.getURI().startsWith("/wallscounter/")) {
            response.setContentFromString(String.valueOf(WallManager.getInstance().howManyWallsExist()), "text/html");
            response.setResponseStatus("200 Ok");
        } else {
            String fullname = baseFolder + "/";
            if (request.getURI().equals("/")) {
                fullname = fullname + "index.html";
            } else {
                fullname = fullname + request.getURI();
            }
            if (response.setContentFromFile(fullname)) {
                response.setResponseStatus("200 Ok");
            } else {
                response.setContentFromString(
                        "<html><body><h1>404 File not found</h1></body></html>",
                        "text/html");
                response.setResponseStatus("404 Not Found");
            }
        }
        response.send(outS);
    }
    
    
    /**
     * Method to delete a wall
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void methodDelete(HTTPmessage request, HTTPmessage response) throws IOException {
        String uri[] = request.getURI().split("/");
        if (request.getMethod().equals("DELETE") && request.getURI().startsWith("/walls/") && uri.length == 3) {
            String wallName = uri[2];
            boolean check = WallManager.getInstance().removeWallByName(wallName);
            System.out.println(wallName);
            System.out.println(check);
            if (check) {
                response.setContentFromString("<html><body>alert(\"The wall has been deleted\")</body></html>", "text/html");
                response.setResponseStatus("200 Ok");
            }
        } else if (request.getMethod().equals("DELETE") && request.getURI().startsWith("/walls/") && uri.length > 3) {
            String wallname = uri[uri.length - 3];
            int messageNumber = Integer.parseInt(uri[uri.length - 1]);

            Wall wall = WallManager.getInstance().findOrCreateWall(wallname);
            if (messageNumber > 0) {
                wall.removeMessage(messageNumber);
                response.setContentFromWall(wall, "text/html");
                response.setResponseStatus("200 Ok");
            }
        } else {
            response.setContentFromString(
                    "<html><body><h1>ERROR: 405 Method Not Allowed</h1></body></html>",
                    "text/html");
            response.setResponseStatus("405 Method Not Allowed");
        }
        response.send(outS);
    }
    
    
    /**
     * Method to post on the wall
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void methodPost(HTTPmessage request, HTTPmessage response) throws IOException {
        if (request.getMethod().equals("POST") && request.getURI().startsWith("/walls/")) {
            String uri[] = request.getURI().split("/");
            String message = uri[uri.length - 1];
            String wallname = uri[uri.length - 2];
            if (!message.isEmpty() && !message.contains("/")) {
                message = message.replace("%20", " ");
                System.out.println(message + " MENSAGEM ");
                WallManager.getInstance().addMessageToWall(wallname, message);
                Wall wall = WallManager.getInstance().findOrCreateWall(wallname);
                response.setContentFromWall(wall, "text/html");
                response.setResponseStatus("200 Ok");
            }

        } else {
            response.setContentFromString(
                    "<html><body><h1>ERROR: 405 Method Not Allowed</h1></body></html>",
                    "text/html");
            response.setResponseStatus("405 Method Not Allowed");
        }
        response.send(outS);
    }
    
    /**
     * Decision on what method is being used
     * 
     */
    public void run() {
        try {
            outS = new DataOutputStream(sock.getOutputStream());
            inS = new DataInputStream(sock.getInputStream());
        } catch (IOException ex) {
            System.out.println("Thread error on data streams creation");
        }
        try {
            HTTPmessage request = new HTTPmessage(inS);
            HTTPmessage response = new HTTPmessage();
            if (request.getMethod().equals("GET")) {
                methodGet(request, response);
            } else if (request.getMethod().equals("POST")) {
                methodPost(request, response);
            } else if (request.getMethod().equals("DELETE")) {
                methodDelete(request, response);
            }
            System.out.println(request.getURI());
        } catch (IOException ex) {
            System.out.println("Thread error when reading request");
        }
        try {
            sock.close();
        } catch (IOException ex) {
            System.out.println("CLOSE IOException");
        }
    }
}
