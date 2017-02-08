package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyActionModel extends JsonLDTypeModel {
    @JsonProperty(value = "actionStatus")
    public String actionStatus;
    @JsonProperty(value = "target")
    public String target;
    @JsonProperty(value = "object")
    public OfferModel event;

    @JsonProperty(value = "agent")
    public Person agent;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Person extends JsonLDTypeModel {

        @JsonProperty(value = "familyName")
        public String familyName;


        @JsonProperty(value = "givenName")
        public String givenName;

        @JsonProperty(value = "email")
        public String email;

       /* @JsonProperty(value = "address")
        public PostalAddress address;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PostalAddress extends JsonLDTypeModel {

            @JsonProperty(value = "addressLocality")
            public PropertyValueSpecificationModel addressLocality;

            @JsonProperty(value = "addressCountry")
            public PropertyValueSpecificationModel addressCountry;
        }*/

    }

    @JsonProperty(value = "result")
    public TicketModel[] result;


}
