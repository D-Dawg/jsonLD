package com.sw17sh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by denni on 1/25/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyValueSpecification extends JsonLDType{

    @JsonProperty(value = "valueRequired")
    public String valueRequired;
    @JsonProperty(value = "defaultValue")
    public String defaultValue;
    @JsonProperty(value = "valueName")
    public String valueName;
}
