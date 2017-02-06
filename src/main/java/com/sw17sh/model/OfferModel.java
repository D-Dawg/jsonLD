package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferModel extends JsonLDTypeModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty(value = "name")
    public String name;

    @JsonProperty(value = "acceptedPaymentMethod")
    private String  acceptedPaymentMethod;

    @JsonProperty(value = "includesObject")
    public TypeAndQuantityNode typeAndQuantityNode;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TypeAndQuantityNode extends JsonLDTypeModel {

        @JsonProperty(value = "amountOfThisGood")
        public String amountOfThisGood;

        @JsonProperty(value = "typeOfGood")
        public TypeofGood typeofGood;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TypeofGood{

            @JsonProperty(value = "@type")
            public String[] types;
        }
    }

    @JsonProperty(value = "priceSpecification")
    public PriceSpecificationModel priceSpecification;

    @JsonProperty(value = "potentialAction")
    public PotentialActionModel potentialAction;

    @JsonProperty(value = "result")
    public TicketModel result;

}
