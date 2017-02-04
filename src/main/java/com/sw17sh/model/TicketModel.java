package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketModel extends JsonLDTypeModel {


    @JsonProperty("underName")
    private String underName;

    @JsonProperty(value = "issuedBy")
    public Object issuedBy;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Object extends JsonLDTypeModel {

        @JsonProperty(value = "@id")
        public String id;
    }

}
