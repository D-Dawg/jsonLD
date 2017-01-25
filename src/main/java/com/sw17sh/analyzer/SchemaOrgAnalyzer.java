package com.sw17sh.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw17sh.Main;
import com.sw17sh.model.WebsiteModel;
import com.sw17sh.model.JsonLDType;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SchemaOrgAnalyzer {
    private final Logger LOGGER = Logger.getLogger(Main.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public String getJsonLD(String filepath){
        try{
            return new Scanner(new File(filepath)).useDelimiter("\\Z").next();
        }catch (IOException e){
            LOGGER.error("Couldnt load file",e);
        }
        return null;
    }

    public void readJsonLD(String jsonLD){
        try {
           JsonLDType jsonLDType = objectMapper.readValue(jsonLD,JsonLDType.class);
            if(jsonLDType != null){
                String type = jsonLDType.getType();
                if (type.equals("WebSite")){
                    WebsiteModel websiteModel = readWebsiteModel(jsonLD);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void writeWebsiteModel(WebsiteModel websiteModel, String newFile){
        if(websiteModel!=null) {
            try {
                objectMapper.writeValue(new File(newFile), websiteModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            LOGGER.error("Cant write json to file.");
        }
    }

}
