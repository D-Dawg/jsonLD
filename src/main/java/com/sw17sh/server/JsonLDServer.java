package com.sw17sh.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw17sh.model.BuyActionModel;
import com.sw17sh.model.EventModel;
import com.sw17sh.model.SearchActionModel;
import com.sw17sh.model.TicketModel;
import com.sw17sh.util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static com.sw17sh.client.JsonLDClient.newVerticalPanel;


public class JsonLDServer {

    public JFrame frame = new JFrame("Event Server");
    public JTextArea messageArea = new JTextArea(40, 40);

    public JsonLDServer() {
        generateGUI();
    }

    private void generateGUI() {
        // Layout GUI

        messageArea.setFont(new Font("Serif", Font.ITALIC, 16));
//        messageArea.setBackground(new Color(0,0,0));
        messageArea.setEditable(false);
        JPanel panel = newVerticalPanel();
        panel.add(new JScrollPane(messageArea));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);
    }


    public static void main(String[] args) throws Exception {
        JsonLDServer server = new JsonLDServer();
        server.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        server.frame.pack();
        server.frame.setVisible(true);
        server.messageArea.append("The JsonLD Event server is now running."+"\n");


        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new EventViewer(listener.accept(), clientNumber++,server).start();
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
        private JsonLDServer server = new JsonLDServer();
        private Socket socket;
        private int clientNumber;
        private ObjectMapper objectMapper = new ObjectMapper();
        private Util util = new Util();
        private StringBuilder jsonInputStringBuilder = new StringBuilder();
        private boolean wait = false;
        BufferedReader in = null;
        PrintWriter out = null;

        private void initializeIO() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public EventViewer(Socket socket, int clientNumber,JsonLDServer server) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.server = server;
            log("New connection with client# " + clientNumber + " at " + socket);
        }


        private void processJsomModel(String input) {
            String modelType = util.getJsonLDModelType(input);
            if (modelType.equals("SearchAction")) {
                processSeachActionModel(input, modelType);
            } else if (modelType.equals("BuyAction")) {
                processBuyActionModel(input, modelType);
            } else {
                out.println("Error not a matching json model found.");
            }
        }

        private EventModel[] loadEventDB(){
            EventModel[] events = null;
            String eventDB = util.getJsonLD("EventDB");
                try {
                    events = objectMapper.readValue(eventDB,EventModel[].class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return events;
        }

        private EventModel[] findAllMatchingEvents(String searchRequest){
            EventModel[] eventsFromDB = loadEventDB();
            ArrayList<EventModel> allMatchingEvents = new ArrayList<EventModel>();
            for (EventModel aEvent:eventsFromDB){
                if(aEvent.name.matches(".*"+searchRequest+".*")){
                    allMatchingEvents.add(aEvent);
                }
            }
            return allMatchingEvents.toArray(new EventModel[0]);
        }

        private String createResponseFromResults(EventModel[] matchingEvents, String searchRequest){
            String response = null;
            if(matchingEvents.length == 0){
                response = "No Events found.";
            }else{
             SearchActionModel responseModel = new SearchActionModel();
             responseModel.context = "http://schema.org";
             responseModel.type = "SearchAction";
             responseModel.actionStatus = "CompletedActionStatus";
             responseModel.target = "http://example.com/search?&q={"+searchRequest+"}";
             responseModel.result = matchingEvents;
             response = util.getJsonFromModel(responseModel);
            }
            return response;
        }


        private void processSeachActionModel(String input, String modelType) {
            initializeIO();
            server.messageArea.append("\nThe Client send a " + modelType + "json."+"\n");
            server.messageArea.append(input+"\n");
            server.messageArea.append("Server processes SearchAction"+"\n");
            server.messageArea.append("\n");
            server.messageArea.append("Server sends SearchAction  with response back:"+"\n");
            String searchActionResponse = generateSearchActionResponseJsonLD(input);
//           server.messageArea.append(searchActionResponse);
            out.println(searchActionResponse);
            wait = true;
            waitForResponse();
        }

        private void processBuyActionModel(String input, String modelType) {
            server.messageArea.append("The Client send a " + modelType + "."+"\n");
            server.messageArea.append(input+"\n");
            server.messageArea.append("Server processes BuyAction"+"\n");
            server.messageArea.append("\n");
            server.messageArea.append("Server sends BuyAction Response:"+"\n");
            String searchActionResponse = generateBuyActionResponseJsonLD(input);

            server.messageArea.append(searchActionResponse);
            out.println(searchActionResponse);

            wait = true;
            waitForResponse();
        }


        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        private void sendWelcomeMessageAndWebSiteJsonLDToClient() {
            // Send a welcome message to the client.
            initializeIO();
            ServerImpl server = new ServerImpl();
            String webSiteJsonLD = server.service1InitialConnect();  //
            out.println("Hello, you are client #" + clientNumber + ".");
            out.println(webSiteJsonLD);
            wait = true;
            waitForResponse();
        }

        private void getJsonFilesAndProcessThem(String input) {
            if (input.equals("") && !jsonInputStringBuilder.toString().equals("")) {
                String completeJson = jsonInputStringBuilder.toString();
                jsonInputStringBuilder = new StringBuilder();
                wait = false;
                processJsomModel(completeJson);
            } else {
                jsonInputStringBuilder.append(input).append("\n");
            }
        }

        private void waitForResponse() {
            while (wait) {
                try {
                    String input = in.readLine();
                    if (input.equals(".")) {
                        break;
                    }
                    getJsonFilesAndProcessThem(input);
                } catch (Exception e) {

                }
            }
        }

        public void run() {
            try {
                sendWelcomeMessageAndWebSiteJsonLDToClient();
            } catch (Exception e) {
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
         * With the searchInput and currentModelFromServer (global) of the json this method is generating a searchAction json,
         * with the data (searchInput) the user defines. In future this inputs can be more than one like location, time frame etc.
         */
        private String generateSearchActionResponseJsonLD(String searchInput) {
//           String searchActionResponse = util.getJsonLD("SearchActionResponse");
            SearchActionModel request = (SearchActionModel) util.getjsonLDModel(searchInput);
            String searchRequest = request.event.name;
            EventModel[] allMatchingEvents = findAllMatchingEvents(searchRequest);
            server.messageArea.append("Found: "+allMatchingEvents.length+" Events.");
            String  searchActionResponse = createResponseFromResults(allMatchingEvents,searchRequest)+"\n";
            return searchActionResponse;
        }

        private String generateBuyActionResponseJsonLD(String buyInput) {
            BuyActionModel request = (BuyActionModel) util.getjsonLDModel(buyInput);

            String ticketJsonString = util.getJsonLD("Ticket");
            TicketModel ticket = (TicketModel) util.getjsonLDModel(ticketJsonString);
            TicketModel[] ticketArray = {ticket};
            request.result = ticketArray;

            // String buyActionResponse = util.getJsonLD("BuyActionResponse");

            String buyActionResponse = util.getJsonFromModel(request);
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
            server.messageArea.append(message);
        }
    }

    ///tresdfg s
}
