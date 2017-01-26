package com.sw17sh.client;

import com.sw17sh.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientImpl {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Util util = new Util();

    /**
     * Main method
     */
    public void run(){

            if(startClient()){
                System.out.println("Connecting to Server");
                String response = requestWebsite();
                /**
                 * The response is the SearchActionSpecification.json
                 * The Client is now fills out the form and sents the server back the
                 * SearchActionRequest.json
                 */
                System.out.println("The Server asks for Details about the EventModel you want to go.");
                String eventName = getEventName();
                String filledOutSearchActionJsonLD = generateFilledOutSearchActionJsonLDFromInput(eventName);
                response = requestEvents(filledOutSearchActionJsonLD);
                /**
                 * The Client creats the SearchActionRequest.json and sends it back to the server.
                 * As a response it gets the SearchAction with filled out results, which is a List of Events.
                 */

            }

    }

    private String generateFilledOutSearchActionJsonLDFromInput(String eventName){

        String filledOutJsonLD = util.jsonFolder + "SearchActionRequest.json";
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

    private boolean startClient(){
        boolean start = false;
        System.out.println("Hello, \n I am your new EventModel assistant. I will help you to find and book Events. \n You are ready to start? (y/n)");
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
         * and get from it as a response of the connection get the SearchActionSpecification.json
         */
        return response;
    }


    private String requestEvents (String filledOutSearchActionJsonLD){
        String response = null;

        /**
         * Connect to server
         * and get from it as a response of the connection get the SearchActionSpecification.json
         */
        return response;
    }

}
