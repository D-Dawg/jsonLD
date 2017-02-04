package com.sw17sh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by denni on 1/25/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceSpecificationModel extends JsonLDTypeModel {

    @JsonProperty(value = "@id")
    public String idLink;

    @JsonProperty(value = "id")
    public String id;

    @JsonProperty(value = "price")
    public String price;

    @JsonProperty(value = "priceCurrency")
    public String priceCurrency;

}
