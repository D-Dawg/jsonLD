package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebsiteModel extends JsonLDTypeModel {

    @JsonProperty("@context")
    public String context;

    @JsonProperty("url")
    public String url;

    @JsonProperty(value = "potentialAction")
    public SearchActionModel searchActionModel;

}
