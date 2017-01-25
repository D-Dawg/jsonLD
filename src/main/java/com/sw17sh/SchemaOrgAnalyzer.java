package com.sw17sh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw17sh.model.WebsiteModel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by denni on 1/25/2017.
 */
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
        }
    }

}
