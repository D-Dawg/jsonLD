package com.sw17sh.server;


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
                String webSiteJsonLD = server.service1InitialConnect().replaceAll("\n", "");

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println(webSiteJsonLD);


                StringBuilder jsonInputStringBuilder = new StringBuilder();
                String line;
                //need a line not empty test ?
                line = in.readLine();
                String hallo = "";
                while (line != null && !line.equals("") && !line.equals("}")) {
                    jsonInputStringBuilder.append(line);
                    System.out.println(line);
                    line = in.readLine();
                    System.out.println();
                }
                boolean test = jsonInputStringBuilder.length() != 0;
                // final } used for termination of data sending from client
                if (jsonInputStringBuilder.length() != 0) {
                    jsonInputStringBuilder.append("}");
                }

                String input = jsonInputStringBuilder.toString();


                String modelType = util.getJsonLDModelType(input);
                if (modelType.equals("SearchAction")) {
                    System.out.println("The Client send a " + modelType + "json with a filled out SearchAction back.");
                    System.out.println(input);
                    System.out.println("Starting Search for events that match.");
                    System.out.println();
                    System.out.println("Server sends SearchAction  with response back:");


                    String searchActionResponse = findSearchActionResponse(input);
                    System.out.println(searchActionResponse);
                    out.println(searchActionResponse);


                    //   SearchActionModel searchActionModel = (SearchActionModel) util.getjsonLDModel(input);
                    //if(searchActionModel!=null){

                    //System.out.println(searchActionModel);
                    //  }
                } else {
                    out.println("Error not a matching json model found.");
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

        private String findSearchActionResponse(String input) {


            return null;
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
