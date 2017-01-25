package com.sw17sh.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


import org.apache.log4j.Logger;



/**
 * Created by denni on 1/25/2017.
 */
public class Util {
    public  final String jsonFolder = "./src/main/java/com/sw17sh/json/";
    private final Logger LOGGER = Logger.getLogger(Util.class);
    public String getJsonLD(String filepath){
        try{
            return new Scanner(new File(filepath)).useDelimiter("\\Z").next();
        }catch (IOException e){
            LOGGER.error("Couldnt load file",e);
        }
        return null;
    }

}
