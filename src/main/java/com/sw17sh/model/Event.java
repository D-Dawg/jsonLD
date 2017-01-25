package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    @JsonProperty("@context")
    private String context;

    @JsonProperty("url")
    private String url;

    @JsonProperty(value = "potentialAction")
    private SearchActionModel searchActionModel;
    @JsonProperty(value = "startTime")
    public String startTime;
    @JsonProperty(value = "endTime")
    public String endTime;

    @JsonProperty(value = "name")
    public PropertyValueSpecificationModel queryInput;
    @JsonProperty(value = "location")
    public SearchActionModel.Place location;
}
