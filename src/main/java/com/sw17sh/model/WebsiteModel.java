package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by ${Dennis} on 05.12.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebsiteModel {

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@context")
    private String context;

    @JsonProperty("url")
    private String url;

    @JsonProperty(value = "potentialAction")
    private SearchActionModel searchActionModel;

}
