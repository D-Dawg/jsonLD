package com.sw17sh.client;

import com.sw17sh.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientImpl {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Util util = new Util();
    private BufferedReader in;
    private PrintWriter out;
    public JFrame frame = new JFrame("Capitalize Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);

    public ClientImpl(){
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        // Add Listeners
        dataField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield
             * by sending the contents of the text field to the
             * server and displaying the response from the server
             * in the text area.  If the response is "." we exit
             * the whole application, which closes all sockets,
             * streams and windows.
             */
            public void actionPerformed(ActionEvent e) {
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
        });
    }

    /**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The EventViewer
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Capitalization Program",
                JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 8001);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
            messageArea.append(in.readLine() + "\n");
        }
    }

    /**
     * Main method
     */
    public void run(){

//            if(startClient()){
//                System.out.println("Connecting to Server");
//                String response = requestWebsite();
//                /**
//                 * The response is the InputSearchActionJsonLD.json
//                 * The Client is now fills out the form and sents the server back the
//                 * FilledOutSearchActionJsonLD.json
//                 */
//                System.out.println("The Server asks for Details about the EventModel you want to go.");
//                String eventName = getEventName();
//                String filledOutSearchActionJsonLD = generateFilledOutSearchActionJsonLDFromInput(eventName);
//                response = requestEvents(filledOutSearchActionJsonLD);
//                /**
//                 * The Client creats the FilledOutSearchActionJsonLD.json and sends it back to the server.
//                 * As a response it gets the SearchAction with filled out results, which is a List of Events.
//                 */
//
//            }

    }

    private String generateFilledOutSearchActionJsonLDFromInput(String eventName){

        String filledOutJsonLD = util.jsonFolder + "FilledOutSearchActionJsonLD.json";
        String filledOutSearchActionJsonLD = null;
        /**
         * read string from file!
         */
        if(eventName.equals("XLETIX")){
            filledOutSearchActionJsonLD = util.getJsonLD(filledOutJsonLD);
        }
        if(eventName.equals("EventModel 2")){
            filledOutSearchActionJsonLD = util.getJsonLD("");
        }
        return filledOutSearchActionJsonLD;
    }

    private String readInput(){
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    private String getEventName(){
        System.out.println("Whats the name of the EventModel?");
        return readInput();
    }

//    private boolean startClient(){
//        boolean start = false;
//        System.out.println("Hello, \n I am your new EventModel assistant. I will help you to find and book Events. \n You are ready to start? (y/n)");
//        String input = readInput();
//        if(input!=null && input.equals("y")) {
//            start = true;
//        }
//        return start;
//    }


    private String requestWebsite (){
        String response = null;

        /**
         * Connect to server
         * and get from it as a response of the connection get the InputSearchActionJsonLD.json
         */
        return response;
    }


    private String requestEvents (String filledOutSearchActionJsonLD){
        String response = null;

        /**
         * Connect to server
         * and get from it as a response of the connection get the InputSearchActionJsonLD.json
         */
        return response;
    }

}
