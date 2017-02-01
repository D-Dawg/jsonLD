package com.sw17sh.server;


import com.sun.corba.se.spi.activation.Server;
import com.sw17sh.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


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
        private StringBuilder jsonInputStringBuilder = new StringBuilder();

        public EventViewer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }


        private void processJsomModel(String input, PrintWriter out) {
            String modelType = util.getJsonLDModelType(input);
            if (modelType.equals("SearchAction")) {
                processSeachActionModel(input, modelType, out);
            } else if (modelType.equals("BuyAction")) {
                processBuyActionModel(input, modelType, out);
            } else {
                out.println("Error not a matching json model found.");
            }
        }

        private void processSeachActionModel(String input, String modelType, PrintWriter out) {
            System.out.println("The Client send a " + modelType + "json.");
            System.out.println(input);
            System.out.println("Server processes SearchAction");
            System.out.println();
            System.out.println("Server sends SearchAction  with response back:");
            String searchActionResponse = generateSearchActionResponseJsonLD(input);
            System.out.println(searchActionResponse);
            out.println(searchActionResponse);
        }

        private void processBuyActionModel(String input, String modelType, PrintWriter out) {
            System.out.println("The Client send a " + modelType + ".");
            System.out.println(input);
            System.out.println("Server processes BuyAction");
            System.out.println();
            System.out.println("Server sends BuyAction Response:");

            String searchActionResponse = generateBuyActionResponseJsonLD(input);
            System.out.println(searchActionResponse);
            out.println(searchActionResponse);
        }


        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */

        private void sendWelcomeMessageAndWebSiteJsonLDToClient(PrintWriter out) {
            // Send a welcome message to the client.
            ServerImpl server = new ServerImpl();
            String webSiteJsonLD = server.service1InitialConnect();  //
            out.println("Hello, you are client #" + clientNumber + ".");
            out.println(webSiteJsonLD);
        }

        private void getJsonFiles(String input, PrintWriter out){
            if(input.equals("")&&!jsonInputStringBuilder.toString().equals("")){
                String completeJson = jsonInputStringBuilder.toString();
                jsonInputStringBuilder = new StringBuilder();
                processJsomModel(completeJson,out);
            }else{
                jsonInputStringBuilder.append(input).append("\n");
            }

        }

        private void waitForResponse(BufferedReader in, PrintWriter out) {

            while (true) {
                try {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }

                    getJsonFiles(input,out);

//                            util.readInputJsonLDScriptNotWaitingInput(in);



                } catch (Exception e) {

                }
            }


        }


        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                sendWelcomeMessageAndWebSiteJsonLDToClient(out);

                waitForResponse(in,out);

                //read SearchActionRequest
//                String input = util.readInputJsonLDScriptNotWaitingInput(in);
//
//                //process and send Search response
//                processJsomModel(input, out);
//
//                //read BuyActionResponse
//                input = util.readInputJsonLDScript(in);
//                //process and send response
//                processJsomModel(input, out);


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

        private String generateSearchActionResponseJsonLD(String searchInput) {
            String searchActionResponse = util.getJsonLD(util.jsonFolder + "SearchActionResponse.json");
            /**
             * With the searchInput and currentModelFromServer (global) of the json this method is generating a searchAction json,
             * with the data (searchInput) the user defines. In future this inputs can be more than one like location, time frame etc.
             */
            return searchActionResponse;
        }

        private String generateBuyActionResponseJsonLD(String buyInput) {
            String buyActionResponse = util.getJsonLD(util.jsonFolder + "BuyActionResponse.json");
            /**
             * With the searchInput and currentModelFromServer (global) of the json this method is generating a searchAction json,
             * with the data (searchInput) the user defines. In future this inputs can be more than one like location, time frame etc.
             */
            return buyActionResponse;
        }


        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }

    ///tresdfg s
}
