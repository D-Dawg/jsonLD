package com.sw17sh;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        try{
            String jsonLDSearchAction = new Scanner (new File("./src/main/java/com/sw17sh/json/InputBuyActionJsonLD")).useDelimiter("\\Z").next();
            System.out.println(jsonLDSearchAction);
        }catch (IOException e){
            LOGGER.error("Couldnt load file",e);
        }

        System.out.println("hello world");




    }
}
