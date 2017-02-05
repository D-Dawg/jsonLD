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
 * Created by denni on 1/25/2017.
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
        String response = util.readInputJsonLDScript(in);
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
        messageArea.append("Do you want to search an Event or book it? (Search/Book)");


        processSearchActionJsonLD();
    }

    /**
     * With the searchInput and currentModelFromServer (global) of the json this method is generating a searchAction json,
     * with the data (searchInput) the user defines. In future this inputs can be more than one like location, time frame etc.
     */
    private String generateSearchActionRequest(String searchInput) {
        String filledOutSearchAction = util.getJsonLD( "SearchActionRequest");
        SearchActionModel model = (SearchActionModel) util.getjsonLDModel(filledOutSearchAction);
        model.event.name = searchInput;
        filledOutSearchAction = util.getJsonFromModel(model)+"\n\n";
        return filledOutSearchAction;
    }

    private String generateFilledOutBuyActionJsonLD(String buyInput) {
        String buyActionRequest = util.getJsonLD( "BuyActionRequest");
        /**
         * With the searchInput and currentModelFromServer (global) of the json this method is generating a searchAction json,
         * with the data (searchInput) the user defines. In future this inputs can be more than one like location, time frame etc.
         */
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
