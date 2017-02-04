package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonLDTypeModel  {

    @JsonProperty("@context")
    public String context;

    public String getType() {
        return type;
    }

    @JsonProperty("@type")
    public String type;

}
