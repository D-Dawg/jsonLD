package com.sw17sh;

import com.sw17sh.model.WebsiteModel;


public class Main {
    private static final String jsonFolder = "./src/main/java/com/sw17sh/json/";
    private static String workingJsonLD = jsonFolder + "InputSearchActionJsonLD";
    private static String saveNewJsonLD = jsonFolder + "ourWebsiteJson.json";


    public static void main(String[] args) {
        SchemaOrgAnalyzer schemaOrgAnalyzer = new SchemaOrgAnalyzer();

        String jsonLD = schemaOrgAnalyzer.getJsonLD(workingJsonLD);
        schemaOrgAnalyzer.readJsonLD(jsonLD);
        WebsiteModel websiteModel = schemaOrgAnalyzer.readWebsiteModel(jsonLD);
        schemaOrgAnalyzer.writeWebsiteModel(websiteModel,saveNewJsonLD);

    }
}
