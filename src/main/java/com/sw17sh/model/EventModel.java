package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventModel extends JsonLDTypeModel {

    @JsonProperty("@context")
    private String context;

    @JsonProperty("url")
    private String url;

    @JsonProperty(value = "name")
    public String name;

    @JsonProperty(value = "potentialAction")
    private SearchActionModel searchActionModel;

    @JsonProperty(value = "startDate")
    public String startDate;

    @JsonProperty(value = "endDate")
    public String endDate;

    @JsonProperty(value = "location")
    public SearchActionModel.Place location;
}
