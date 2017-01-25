package com.sw17sh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw17sh.model.WebsiteModel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    private static String workingJsonLD = "./src/main/java/com/sw17sh/json/InputSearchActionJsonLD";
    private static final Logger LOGGER = Logger.getLogger(Main.class);


    private static String getJsonLD(String filepath){
        try{
            return new Scanner (new File(filepath)).useDelimiter("\\Z").next();
        }catch (IOException e){
            LOGGER.error("Couldnt load file",e);
        }
        return null;
    }

    private static WebsiteModel getWebsiteModel(String jsonLDWebsite){
        if(jsonLDWebsite!=null){
            ObjectMapper objectMapper = new ObjectMapper();
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

    public static void main(String[] args) {
        String jsonLD = getJsonLD(workingJsonLD);
        WebsiteModel websiteModel = getWebsiteModel(jsonLD);
        if(websiteModel!=null){
            String stringWebsiteModelJson =
        }



    }
}
