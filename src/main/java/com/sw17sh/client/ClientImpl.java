package com.sw17sh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by denni on 1/25/2017.
 */
public class ClientImpl {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Main method
     */
    public void run(){

            if(startClient()){
                System.out.println("Connecting to Server");
                String response = requestWebsite();
                /**
                 * The response is the InputSearchActionJsonLD.json
                 * The Client is now fills out the form and sents the server back the
                 * FilledOutSearchActionJsonLD.json
                 */
                System.out.println("The Server asks for Details about the Event you want to go.");
                String eventName = getEventName();
                String filledOutSearchActionJsonLD = generateFilledOutSearchActionJsonLDFromInput(eventName);
                response = requestEvents(filledOutSearchActionJsonLD);
                /**
                 * The Client creats the FilledOutSearchActionJsonLD.json and sends it back to the server.
                 * As a response it gets the SearchAction with filled out results, which is a List of Events.
                 */

            }

    }

    private String generateFilledOutSearchActionJsonLDFromInput(String eventName){
        String filledOutSearchActionJsonLD = null;
        /**
         * read string from file!
         */
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
        System.out.println("Whats the name of the Event?");
        return readInput();
    }

    private boolean startClient(){
        boolean start = false;
        System.out.println("Hello, \n I am your new Event assistant. I will help you to find and book Events. \n You are ready to start? (y/n)");
        String input = readInput();
        if(input!=null && input.equals("y")) {
            start = true;
        }
        return start;
    }


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
