package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketModel extends JsonLDTypeModel {


    @JsonProperty("underName")
    public String underName;

    @JsonProperty("url")
    public String url;

    @JsonProperty("name")
    public String name;

    @JsonProperty("totalPrice")
    public int totalPrice;

    @JsonProperty("dateIssued")
    public String dateIssued;

    @JsonProperty("ticketNumber")
    public int ticketNumber;


    @JsonProperty(value = "issuedBy")
    public Organization issuedBy;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Organization extends JsonLDTypeModel {

        @JsonProperty(value = "name")
        public String name;

        @JsonProperty(value = "url")
        public String url;

        @JsonProperty(value = "id")
        public String id;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Object extends JsonLDTypeModel {

        @JsonProperty(value = "id")
        public String id;
    }

}
