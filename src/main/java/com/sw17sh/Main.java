/*
package com.sw17sh;

import com.sw17sh.model.WebsiteModel;
import com.sw17sh.util.SchemaOrgAnalyzer;
import com.sw17sh.util.Util;


public class Main {


    public static void main(String[] args) {
        SchemaOrgAnalyzer schemaOrgAnalyzer = new SchemaOrgAnalyzer();
        Util util = new Util();
        String workingJsonLD = util.jsonFolder + "SearchActionRequest.json";
        String saveNewJsonLD = util.jsonFolder + "ourWebsiteJson.json";


        String jsonLD = util.getJsonLD(workingJsonLD);
        schemaOrgAnalyzer.readJsonLD(jsonLD);
        WebsiteModel websiteModel = schemaOrgAnalyzer.readWebsiteModel(jsonLD);
        schemaOrgAnalyzer.writeWebsiteModel(websiteModel,saveNewJsonLD);

    }
}
*/
