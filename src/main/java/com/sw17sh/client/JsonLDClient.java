package com.sw17sh.client;

import com.sw17sh.model.JsonLDTypeModel;
import com.sw17sh.util.Util;

import javax.swing.*;
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
    private JTextField dataField = new JTextField(80);
    private JTextArea messageArea = new JTextArea(30, 60);
    private Util util = new Util();
    private JsonLDTypeModel currentModelFromServer = null;


    private void processWebsiteModel(String dataFieldInput) {

        //Parsing user input
        String searchActionRequest = generateSearchActionRequest(dataFieldInput).replaceAll("\r\n", "");

        //Send SearchActionRequest
        out.println(searchActionRequest);
        messageArea.append(dataFieldInput + "\n" + "\n");
        messageArea.append("Send Search Action Request:" + "\n" + "\n");
        messageArea.append(searchActionRequest);

        //read SearchActionResponse
        String response = util.readInputJsonLDScript(in);
        messageArea.append("Receive Search Action Response:" + "\n" + "\n");
        messageArea.append(response + "\n" + "\n");

        //Send BuyactionRequest
            String buyActionRequest = generateFilledOutBuyActionJsonLD(response);
        messageArea.append("Send Buy Action Request:" + "\n" + "\n");
        messageArea.append(buyActionRequest + "\n" + "\n");
            out.println(buyActionRequest);

        //read and print BuyActionResponse
        response = util.readInputJsonLDScript(in);
        messageArea.append("Receive Buy Action Response:");
        messageArea.append(response + "\n" + "\n");

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
        if (currentModelFromServer.getType().equals("WebSite")) {
            processWebsiteModel(dataFieldInput);
        } else {
            noJsonModel();
        }

    }

    public JsonLDClient() {


        // Layout GUI
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "South");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

//         Add Listeners
        dataField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String dataFieldInput = dataField.getText();
                processJsonModel(dataFieldInput);
            }
        });
    }


    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to your Event-assistant.",
                JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        messageArea.append(in.readLine() + "\n");


        StringBuilder jsonInputStringBuilder = new StringBuilder();
        String line = in.readLine();
        System.out.println();
        //need a line not empty test ?
        while (line != null && !line.equals("}") && !line.equals("")) {
            jsonInputStringBuilder.append(line + "\n");
            System.out.println(line);
            line = in.readLine();
            System.out.println();
        }
        // final } used for termination of data sending from client
        jsonInputStringBuilder.append("}");
        String jsonSearchAction = jsonInputStringBuilder.toString();


        messageArea.append("Receive Search Action Specification" + "\n");
        messageArea.append(jsonSearchAction + "\n");

        currentModelFromServer = util.getjsonLDModel(jsonSearchAction + "\n"

        );
        messageArea.append("Whats the name of the event you want to go to?" + "\n");

    }

    private String generateSearchActionRequest(String searchInput) {
        String filledOutSearchAction = util.getJsonLD(util.jsonFolder + "SearchActionRequest.json");
        /**
         * With the searchInput and currentModelFromServer (global) of the json this method is generating a searchAction json,
         * with the data (searchInput) the user defines. In future this inputs can be more than one like location, time frame etc.
         */
        return filledOutSearchAction;
    }

    private String generateFilledOutBuyActionJsonLD(String buyInput) {
        String buyActionRequest = util.getJsonLD(util.jsonFolder + "BuyActionRequest.json");
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
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }

}
