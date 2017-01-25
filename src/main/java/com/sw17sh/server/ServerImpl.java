package com.sw17sh.server;

import com.sw17sh.util.Util;

/**
 * Created by dennis on 1/25/2017.
 * Server erhält unterschiedliche schemaorg.json, seine Aufgabe ist es anhand deren den richtigen service auszuführen.
 */
public class ServerImpl {
    private Util util = new Util();

    /**
     * The first time the client connects with the server.
     * The server now sends the client a InputSearchActionJsonLD, which the client than can fill out and send back.
     * @return InputSearchActionJsonLD
     */
    private String service1InitialConnect(){
        return util.getJsonLD(util.jsonFolder+"InputSearchActionJsonLD.json");

    }

    private void sendJsonLDToClient(String jsonLDToSend){

    }

    public void run(){
        System.out.println("Server started.");
        /**
         * wait for client
         */
        if(true){
            /**
             * As response of client connect Send Client InputSearchActionJsonLD
             */
            String inputSearchActionJsonLD = service1InitialConnect();
            System.out.println(inputSearchActionJsonLD);
        }

    }

}
