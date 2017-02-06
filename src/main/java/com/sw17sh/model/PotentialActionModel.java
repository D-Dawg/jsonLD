package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PotentialActionModel {

    @JsonProperty("object")
    private Object object;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Object extends JsonLDTypeModel {

        @JsonProperty(value = "@id")
        public String id;
    }

    @JsonProperty(value = "seller")
    public SellerModel seller;

    @JsonProperty(value = "actionStatus")
    public String actionStatus;

    @JsonProperty(value = "entrypoint")
    public String entrypoint;

    @JsonProperty(value = "priceSpecification")
    public PriceSpecificationModel priceSpecification;





}
