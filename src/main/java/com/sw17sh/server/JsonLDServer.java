package com.sw17sh.server;


import com.sw17sh.model.JsonLDTypeModel;
import com.sw17sh.model.WebsiteModel;
import com.sw17sh.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by denni on 1/25/2017.
 */
public class JsonLDServer {



    public static void main(String[] args) throws Exception {
        System.out.println("The JsonLD Event server is now running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new EventViewer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class EventViewer extends Thread {
        private Socket socket;
        private int clientNumber;
        private Util util = new Util();

        public EventViewer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {
                ServerImpl server = new ServerImpl();
                String webSiteJsonLD = server.service1InitialConnect().replaceAll("\r\n","");

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println(webSiteJsonLD);



                // Get messages from the client, line by line; return them
                // capitalized
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    String modelType = util.getJsonLDModelType(input);
                    if(modelType.equals("WebSite")){
                        System.out.println("The Client send a "+modelType+"json with a filled out SearchAction back.");
                        System.out.println(input);
                        System.out.println("Starting Search for events that match.");
                        WebsiteModel filledOutWebsite = (WebsiteModel) util.getjsonLDModel(input);
                        if(filledOutWebsite!=null){

                        }
                    }else {
                        out.println("Error not a matching json model found. ");
                    }





                }
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }

}
