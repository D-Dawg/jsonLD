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
    public EventModel event;
    // @JsonProperty(value = "result-output")
    //public String resultOutput;
    @JsonProperty(value = "result")
    public TicketModel[] result;


}
