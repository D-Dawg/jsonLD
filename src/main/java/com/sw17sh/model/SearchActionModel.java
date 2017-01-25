package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by ${Dennis} on 05.12.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchActionModel {
    @JsonProperty(value = "actionStatus")
    public String actionStatus;
    @JsonProperty(value = "target")
    public String target;
    @JsonProperty(value = "object")
    public Event event;
    @JsonProperty(value = "startTime")
    public String startTime;
    @JsonProperty(value = "endTime")
    public String endTime;
    @JsonProperty(value = "query-input")
    public PropertyValueSpecification queryInput;
    @JsonProperty(value = "location")
    public Place location;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place {
        @JsonProperty(value = "address")
        public PostalAddress address;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PostalAddress {
            @JsonProperty(value = "addressLocality-input")
            public PropertyValueSpecification addressLocalityInput;

            @JsonProperty(value = "addressCountry-input")
            public PropertyValueSpecification addressCountryInput;
        }







    }
}
