package com.sw17sh.client;

import com.sw17sh.model.JsonLDTypeModel;
import com.sw17sh.model.SearchActionModel;
import com.sw17sh.util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Prototypical implementation of a client
 * It connects to the server and can process the content of the website automatically
 * based on jsonLD files he receives:
 * For a rough idea refer to presentation
 * The folder json contains files which show this communication exemplarily.
 * 1. Client connects and receives the >> SearchActionSpecification << from Server
 * 2. Client fills out information and sends back the >> SearchActionRequest <<
 * 3. Server receives Request processes Data and sends back >> SearchActionResponse <<
 * 4. Client receives response, processes it and sends back the >> BuyActionRequest <<
 * 5. Server receives request, processes Data and sends back >> BuyActionResponse <<
 */
public class JsonLDClient {
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Event Client");
    private JTextField dataField = new JTextField(60);
    private JTextArea messageArea = new JTextArea(40,40);
    private Util util = new Util();
    private JsonLDTypeModel currentModelFromServer = null;
    private JButton sizeButton = new JButton("+");
    private JLabel label = new JLabel("Your Answer:");
    private JLabel label2 = new JLabel("");
    private String state = null;

    /**
     * The client received the SearchActionSpecification, which is a website containing a potential SearchAction
     * It fills out the SearchAction, with required parameters. In this case the parameter is representing the Name of Event.
     * @param dataFieldInput should be:
     *                       Name of event
     *                       Time or Time interval of Event
     *                       Place/Location
     */
    private void processWebsiteModel(String dataFieldInput) {
        //Parsing user input
        String searchActionRequest = generateSearchActionRequest(dataFieldInput);
        //Send SearchActionRequest
        out.println(searchActionRequest);
        messageArea.append("Send Search Action Request:" + "\n" + "\n");
        messageArea.append(dataFieldInput + "\n" + "\n");
//        messageArea.append(searchActionRequest);
        //read SearchActionResponse
        String response = util.readInputJsonLDScriptNotWaitingInput(in);
        if(response.matches("No Events found.\n}")){
            label.setText("No Matching Events found. Search again");
        }else{
            currentModelFromServer = util.getjsonLDModel(response + "\n");
            SearchActionModel serverResponse = (SearchActionModel) currentModelFromServer;
            label.setText("The Server found: "+serverResponse.result.length+" Events.");
            messageArea.append("Receive Search Action Response:" + "\n" + "\n");
            messageArea.append(response + "\n" + "\n");
            label2.setText("Choose an Event you want to go to \n Which offer do you choose? Eventname,OfferNo");
        }
    }

    private void processSearchActionResponse(String dataFieldInput) {
        //Send BuyactionRequest
        String buyActionRequest = generateFilledOutBuyActionJsonLD(dataFieldInput);
        messageArea.append("Send Buy Action Request:" + "\n" + "\n");
        messageArea.append(dataFieldInput+"\n");
//        messageArea.append(buyActionRequest + "\n" + "\n");
        out.println(buyActionRequest);
        out.flush();
        //read and print BuyActionResponse
        String response = util.readInputJsonLDScript2(in);
        messageArea.append("Receive Buy Action Response:"+"\n");
        messageArea.append( response + "\n" + "\n");
        label.setText("Thank you for buying a Ticket with us."+"\n");
        dataField.setEnabled(false);
    }

    private void noJsonModel() {
        out.println(dataField.getText());
        String response;
        try {
            response = in.readLine();
            if (response == null || response.equals("")) {
                System.exit(0);
            }
        } catch (IOException ex) {
            response = "Error: " + ex;
        }
        messageArea.append(response + "\n");
        dataField.selectAll();
    }


    private void processJsonModel(String dataFieldInput) {
//        if(state.equals("search")){
//
//        }else if(state.equals("book")){
//
//        }
        if (currentModelFromServer.getType().equals("WebSite")) {
            processWebsiteModel(dataFieldInput);
        } else if(currentModelFromServer.getType().equals("SearchAction")){
            processSearchActionResponse(dataFieldInput);
        }
        else{
            noJsonModel();
        }

    }

    public static JPanel newVerticalPanel() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        return panel;
    }

    /**
     * GUI Layout
     */
    private void generateGUI(){
        // Layout GUI
        dataField.setPreferredSize( new Dimension( 200, 50 ) );
        messageArea.setFont(new Font("Serif", Font.ITALIC, 16));
        messageArea.setBackground(new Color(211,211,211));
        messageArea.setEditable(false);
        label.setPreferredSize( new Dimension(200,50));
        label2.setPreferredSize( new Dimension(200,20));
        JPanel panel = newVerticalPanel();
        panel.add(sizeButton);
        panel.add(new JScrollPane(messageArea));
        panel.add(label);
        panel.add(label2);
        panel.add(dataField);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);
    }

    private void generateActionListners(){
        //         Add Listeners
        dataField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dataFieldInput = dataField.getText();
                processJsonModel(dataFieldInput);
                dataField.setText("");
                /**
                 * The Server can receive either an Search or BuyAction and processes them independently.
                 * The idea of this code-snipet was to show this feature.
                 */
//                if(state==null){
//                    if(dataFieldInput.matches(".*search") || dataFieldInput.matches(".*book.*") ){
//                        state = dataFieldInput;
//                    }else{
//                        label2.setText("Illegal Input, type search or book.");
//                        dataField.setText("");
//                    }
//                }else{
//                    processJsonModel(dataFieldInput);
//                    dataField.setText("");
//                }
            }
        });
        sizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Font font = messageArea.getFont();
                float size = font.getSize() + 1.0f;
                messageArea.setFont( font.deriveFont(size) );
            }
        });

    }

    /**
     * Constructor Client
     */
    public JsonLDClient() {
        generateGUI();
        generateActionListners();
    }

    /**
     * Calls an OptionPane for user to enter server address
     * @return server address
     */
    private Socket getServerSocketFromUserInput(){
        // Get the server address from a dialog box.
        try{
            String serverAddress =  (String) JOptionPane.showInputDialog(
                    frame,
                    "Enter IP Address of the Server:",
                    "Welcome to your Event-assistant.",
                    JOptionPane.QUESTION_MESSAGE,null,null,(new String("localhost")));
            return new Socket(serverAddress, 9898);
        }catch (IOException e){

        }
        return null;
    }


    private void initializeMessageIO(Socket socket){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Client receives on the initial connect to the Server the json containing a website and a potential Action SearchAction
     * To complete the potential Action and send back the filled out Search Action, input is required.
     * (Name, Place, Time of Event)
     */
    private void processSearchActionJsonLD(){
        //Read and Print SeachArctionSpecification
        messageArea.append("\nReceive Search Action Specification:" + "\n");
        String jsonSearchAction = util.readInputJsonLDScriptNotWaitingInput(in);
        messageArea.append(jsonSearchAction + "\n" + "\n");
        currentModelFromServer = util.getjsonLDModel(jsonSearchAction + "\n");
        label.setText("Whats the name of the event you want to go to?");
        label2.setText("Hints; The Name in The DB are XLETIX1, XLETIX2, XLETIX3");
        messageArea.append(  "\n" + "\n");
    }

    /**
     * Connects to the Server which the user specifies,
     * initializes the IO Reader,
     * consumes the welcoming message,
     * receives a WebSite jsonLD file with an potential SearchAction
     * asks the user for more details to fill out the SearchAction
     * @throws IOException
     */
    public void connectToServer() throws IOException {

        // Make connection and initialize streams
        Socket socket = getServerSocketFromUserInput();
        initializeMessageIO(socket);

        // Consume the initial welcoming messages from the server
        messageArea.append(in.readLine() + "\n");
//        messageArea.append("Do you want to search an Event or book it? (Search/Book)");


        processSearchActionJsonLD();
    }

    /**
     * Client uses parameter to generate a SearchActionRequest.
     * Based on the SearchActionSpecification
     * @param searchInput exemplarily for searchParameters
     *                    Event name
     *                    Event period/time
     *                    Event location/place
     * @return the jsonLD which will be send as the Request to the Server
     */
    private String generateSearchActionRequest(String searchInput) {
        String filledOutSearchAction = util.getJsonLD( "SearchActionRequest");
        SearchActionModel model = (SearchActionModel) util.getjsonLDModel(filledOutSearchAction);
        model.event.name = searchInput;
        filledOutSearchAction = util.getJsonFromModel(model)+"\n\n";
        return filledOutSearchAction;
    }

    /**
     * Here the client received the SearchActionResponse from the Server after sending the SearchActionRequest.
     * It is now filling out the potential BuyAction he received in the Response.
     * The client should be implemented the way, that it now specifies all information to buy the Required Ticket.
     * @param buyInput prototypical should be parameters like:
     *                 Name of Buyer
     *                 Eventname of Events in SearchActionResponse
     *                 Which Ticket offer he wants to choose in case there are multiple
     *                 Amount of Tickets he wants to buy
     *                  --> mapped with the property: includesObject inside the SearchActionResponse
     * @return
     */
    private String generateFilledOutBuyActionJsonLD(String buyInput) {
        String buyActionRequest = util.getJsonLD( "BuyActionRequest");
        //missing implementation (similar to generateSearchActionRequest(String searchInput))
        return buyActionRequest;
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        JsonLDClient client = new JsonLDClient();
        client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }

}
