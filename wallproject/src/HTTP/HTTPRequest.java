/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HTTP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Miguel Santos <1161386@isep.ipp.pt>
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

    private void methodGet(HTTPmessage request, HTTPmessage response) throws IOException {
        if (request.getURI().equals("/walls")) {
            response.setContentFromString(
                    HTTPServer.getVotesStandingInHTML(), "text/html");
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

    private void methodDelete(HTTPmessage request, HTTPmessage response) throws IOException {

    }

    private void methodPut(HTTPmessage request, HTTPmessage response) throws IOException {
        if (request.getMethod().equals("PUT")
                && request.getURI().startsWith("/votes/")) {
            HTTPServer.castVote(request.getURI().substring(7));
            response.setResponseStatus("200 Ok");
        } else {
            response.setContentFromString(
                    "<html><body><h1>ERROR: 405 Method Not Allowed</h1></body></html>",
                    "text/html");
            response.setResponseStatus("405 Method Not Allowed");
        }
        response.send(outS);
    }

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
            System.out.println(request.getURI());

            if (request.getMethod().equals("GET")) {
                methodGet(request, response);
            } else { 
                methodPut(request, response);
            }
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
