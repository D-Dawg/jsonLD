package com.sw17sh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ddawg on 2/4/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceModel extends JsonLDTypeModel {

    @JsonProperty(value = "address")
    public PostalAddress address;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PostalAddress extends JsonLDTypeModel {

        @JsonProperty(value = "addressLocality-input")
        public PropertyValueSpecificationModel addressLocalityInput;

        @JsonProperty(value = "addressCountry-input")
        public PropertyValueSpecificationModel addressCountryInput;
    }
}