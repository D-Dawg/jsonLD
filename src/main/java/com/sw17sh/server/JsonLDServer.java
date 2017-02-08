package com.sw17sh.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw17sh.model.*;
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

/**
 * Prototypical implementation of server!
 * It waits for a client to connect if so it sends a Specification of the website containing a potential SearchAction
 * It is implemented the way right now that it can receive either a Search or BuyAction and respond to its input.
 * It cant yet inspect all values of correctness or manipulate them. It is exemplarily done in the Search and BuyAction.
 * In the SearchAction it uses for example the Event name and searches the database for all matching ones
 *  - multiple matches possible try XLETIX it will match 3 Events.
 *  The matches are then used to generate a List of events (Array), which gets attached to the SearchActionResponse as the results.
 * In the BuyAction the Request of the client is used to
 * exemplarily adjust the values of
 *  - undername, the name of the Buyer
 *  - result, which gets a Ticket attached to it
 *  - actionStatus, which is not an CompletedActionStatus
 * generate the response and send it back to the client
 */
public class JsonLDServer {
    public JFrame frame = new JFrame("Event Server");
    public JTextArea messageArea = new JTextArea(40, 40);

    /**
     * Constructor server
     */
    public JsonLDServer() {
        generateGUI();
    }

    /**
     * GUI Layout
     */
    private void generateGUI() {
        messageArea.setFont(new Font("Serif", Font.ITALIC, 16));
        messageArea.setEditable(false);
        JPanel panel = newVerticalPanel();
        panel.add(new JScrollPane(messageArea));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);
    }

    /**
     * runs the server application
     * @param args
     * @throws Exception
     */
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
     * A private thread to handle requests on a particular socket.
     * The client terminates the dialogue by sending a single line
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


        /**
         * Client sends a Request, depending on its type the Server calls the correct method to respond to request.
         * @param input the json requst the client send.
         */
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

        /**
         * Reads the EventDB.json to get all Evens saved in the prototypical DB.
         * @return
         */
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

        /**
         * Looks up in the DB which events are matching the search criteria provided as parameter
         * @param searchRequest the name of the event the client is looking for
         * @return all Events, which contain the name of the @param
         */
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

        /**
         * Uses the matched results from the DB and the name of the event the client was looking for
         * and generates the response, which is send back to the client.
         * @param matchingEvents all matching events from the DB (EventDB.json)
         * @param searchRequest the searchRequest is the name of the Event the client was looking
         * @return the SearchActionResponse, with adjusted values to the Request. CompletedActionStatus is set and so on...
         */
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

        /**
         * Is triggered when the client sends a SearchAction to the server.
         *  The Server processes the SearchAction and generates a response according to its values
         * @param input the SearchActionRequest the client send
         * @param modelType used for output the modelType the client send (helper)
         */
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

        /**
         * Is triggered when the server sends a BuyAction to the server.
         * The Server processes the BuyAction and generates a response according to its values
         * @param input the BuyActionRequest the client send
         * @param modelType used for output the modelType the client send (helper)
         */
        private void processBuyActionModel(String input, String modelType) {
            server.messageArea.append("The Client send a " + modelType + "."+"\n");
            server.messageArea.append(input+"\n");
            server.messageArea.append("Server processes BuyAction"+"\n");
            server.messageArea.append("\n");
            String searchActionResponse = generateBuyActionResponseJsonLD(input);
            server.messageArea.append("\n");
            server.messageArea.append("Server sends BuyAction Response:"+"\n");
            server.messageArea.append(searchActionResponse);
            out.println(searchActionResponse);
            wait = true;
            waitForResponse();
        }


        /**
         * client connects and gets a welcome message and
         * The specification of the service, which the client connected to:
         *      a website containing a potential SearchAction
         */
        private void sendWelcomeMessageAndWebSiteJsonLDToClient() {
            // Send a welcome message to the client.
            initializeIO();
            String webSiteJsonLD = service1InitialConnect();  //
            out.println("Hello, you are client #" + clientNumber + ".");
            out.println(webSiteJsonLD);
            wait = true;
            waitForResponse();
        }

        /**
         * The first time the client connects with the server.
         * The server sends the client a SearchActionSpecification, which is a Website containing the potential SearchAction
         * @return SearchActionSpecification
         */
        private String service1InitialConnect(){
            return util.getJsonLD("SearchActionSpecification");
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

        /**
         * Runs when client connects
         */
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
         * generates the SearchActionResponse by using the json of the client (SearchActionRequest)
         * it gets the evntsname the client is looking for and looks up all matching events from the DB (EventDB.json)
         * Afterwards it adds the List of matching Events to rhe response and returns it
         * @param searchInput the SearchActionRequest received from client
         * @return SearchActionResponse with results and CompletedActionStatus
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

        /**
         * generates a BuyActionResponse to the BuyRequest of the client.
         * it uses the name from the request to set it inside the ticket
         * it gets the number of tickets inside the offer,
         *  so far it just outputs it onto the server panel, the loop is prototypically implemented
         * it sets the ActionStatus to CompletedActionStatus
         * @param buyInput it is the json received by the client and is a request to buy a ticket to an event
         * @return the success full BuyActionResponse which gets send back to the client
         */
        private String generateBuyActionResponseJsonLD(String buyInput) {
            BuyActionModel request = (BuyActionModel) util.getjsonLDModel(buyInput);
            String familyName = request.agent.familyName;
            String firstName = request.agent.givenName;
            String fullName = firstName + " " + familyName;
            String ticketJsonString = util.getJsonLD("Ticket");
            OfferModel offer = request.event;
            String numberOfTickets = offer.typeAndQuantityNode.amountOfThisGood;
            int numberOfTicketsInt = Integer.parseInt(numberOfTickets);
            server.messageArea.append("\n The client wants to buy: " + numberOfTickets + " Tickets."+"\n");
            for(int i=0; i<numberOfTicketsInt; i++){
                // Here would be the loop to add the number of tickets in the result dynamically
            }
            TicketModel ticket = (TicketModel) util.getjsonLDModel(ticketJsonString);
            ticket.underName = fullName;
            // ticket.issuedBy="Event Web Assistant";
            TicketModel[] ticketArray = {ticket};
            request.result = ticketArray;
            request.actionStatus = "CompletedActionStatus";
            // String buyActionResponse = util.getJsonLD("BuyActionResponse");
            String buyActionResponse = util.getJsonFromModel(request);
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
