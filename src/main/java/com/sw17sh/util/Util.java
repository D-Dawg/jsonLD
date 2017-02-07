package com.sw17sh.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw17sh.model.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



/**
 * Created by denni on 1/25/2017.
 */
public class Util {
    public  final String jsonFolder = "./src/main/java/com/sw17sh/json/";
    private final Logger LOGGER = Logger.getLogger(Util.class);
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private ObjectMapper objectMapper = new ObjectMapper();


    public String getJsonLD(String filepath){
        try{
            return new Scanner(new File(jsonFolder+filepath+".json")).useDelimiter("\\Z").next();
        }catch (IOException e){
            LOGGER.error("Couldnt load file",e);
        }
        return null;
    }


    public boolean startClient(){
        boolean start = false;
        System.out.println("Hello, \n I am your new EventModel assistant. I will help you to find and book Events. \n You are ready to start? (y/n)");
        String input = readInput();
        if(input!=null && input.equals("y")) {
            start = true;
        }
        return start;
    }

    public String readInputJsonLDScript(BufferedReader in) {
        StringBuilder jsonInputStringBuilder = new StringBuilder();

        //read the input
        String line = null;
        try {
            line = in.readLine();

            //Empty ness check here or not ?
            while (line != null && !line.equals("}")) {
                jsonInputStringBuilder.append(line).append("\n");
                System.out.println(line);
                line = in.readLine();
                System.out.println();
            }

            // final } used for termination of data sending from client
            if (jsonInputStringBuilder.length() != 0) {
                jsonInputStringBuilder.append("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = jsonInputStringBuilder.toString();
        return response;
    }

    public String readInputJsonLDScriptNotWaitingInput(BufferedReader in) {
        StringBuilder jsonInputStringBuilder = new StringBuilder();

        //read the input
        String line = null;
        try {
            line = in.readLine();
            //Empty ness check here or not ?
            while (line != null && !line.equals("}") && !line.equals("") &&!line.equals("\n")) {
                jsonInputStringBuilder.append(line).append("\n");
                System.out.println(line);
                line = in.readLine();
                System.out.println();
            }

            // final } used for termination of data sending from client
            if (jsonInputStringBuilder.length() != 0) {
                jsonInputStringBuilder.append("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = jsonInputStringBuilder.toString();
        return response;
    }

    public String readInput(){
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public String getJsonLDModelType(String jsonLD){
        try {
            JsonLDTypeModel jsonLDType = objectMapper.readValue(jsonLD,JsonLDTypeModel.class);
            if(jsonLDType != null){
                return jsonLDType.getType();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonLDTypeModel getjsonLDModel(String jsonLD){
        JsonLDTypeModel jsonLDModel = null;
        String jsonLDtype = getJsonLDModelType(jsonLD);
        if (jsonLDtype.equals("WebSite")){
            jsonLDModel = readWebsiteModel(jsonLD);
        } else if (jsonLDtype.equals("SearchAction")) {
            System.out.println("Search Action");
            jsonLDModel = readSearchActionModel(jsonLD);
        } else if (jsonLDtype.equals("BuyAction")) {
            System.out.println("Buy Action");
            jsonLDModel = readBuyActionModel(jsonLD);
        } else if (jsonLDtype.equals("Ticket")) {
            System.out.println("Ticket");
            jsonLDModel = readTicketModel(jsonLD);
        }
        return jsonLDModel;
    }

    public TicketModel readTicketModel(String jsonLDTicket) {
        if (jsonLDTicket != null) {
            try {
                return objectMapper.readValue(jsonLDTicket, TicketModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.error("Empty String cant continue");
        }
        return null;
    }

    private JsonLDTypeModel readBuyActionModel(String jsonLDBuyAction) {
        if (jsonLDBuyAction != null) {
            try {
                return objectMapper.readValue(jsonLDBuyAction, BuyActionModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.error("Empty String cant continue");
        }
        return null;
    }

    private JsonLDTypeModel readSearchActionModel(String jsonLDSerachACtion) {
        if (jsonLDSerachACtion != null) {
            try {
                return objectMapper.readValue(jsonLDSerachACtion, SearchActionModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.error("Empty String cant continue");
        }
        return null;
    }

    public WebsiteModel readWebsiteModel(String jsonLDWebsite){
        if(jsonLDWebsite!=null){
            try {
                return objectMapper.readValue(jsonLDWebsite,WebsiteModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            LOGGER.error("Empty String cant continue");
        }
        return null;
    }

    public String getJsonFromModel(JsonLDTypeModel model){
        String modelAsJson = null;
        if(model!=null) {
            try {
                modelAsJson =  objectMapper.writeValueAsString(model);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            LOGGER.error("Cant write json to file.");
        }
        return modelAsJson;
    }
}
