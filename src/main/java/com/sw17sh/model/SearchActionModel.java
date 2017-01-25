package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchActionModel extends JsonLDTypeModel {
    @JsonProperty(value = "actionStatus")
    public String actionStatus;
    @JsonProperty(value = "target")
    public String target;
    @JsonProperty(value = "object")
    public EventModel event;
    @JsonProperty(value = "result-output")
    public String resultOutput;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place extends JsonLDTypeModel {

        @JsonProperty(value = "address")
        public PostalAddress address;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PostalAddress extends JsonLDTypeModel {
            @JsonProperty("@type")
            private String type;
            @JsonProperty(value = "addressLocality-input")
            public PropertyValueSpecificationModel addressLocalityInput;

            @JsonProperty(value = "addressCountry-input")
            public PropertyValueSpecificationModel addressCountryInput;
        }







    }
}
