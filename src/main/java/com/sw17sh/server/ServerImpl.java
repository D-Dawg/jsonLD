package com.sw17sh.server;

import com.sw17sh.util.Util;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by dennis on 1/25/2017.
 * Server erhält unterschiedliche schemaorg.json, seine Aufgabe ist es anhand deren den richtigen service auszuführen.
 */
public class ServerImpl{
    private Util util = new Util();
    private final Logger LOGGER = Logger.getLogger(ServerImpl.class);


    /**
     * The first time the client connects with the server.
     * The server now sends the client a InputSearchActionJsonLD, which the client than can fill out and send back.
     * @return InputSearchActionJsonLD
     */
    public String service1InitialConnect(){
        return util.getJsonLD(util.jsonFolder+"SearchActionSpecification.json");
    }


}
