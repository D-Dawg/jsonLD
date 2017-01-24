package com.sw17sh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by ${Dennis} on 05.12.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchActionModel {
    @JsonProperty(value = "result")
    public CPHResult result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CPHResult {

        @JsonProperty(value = "routes")
        private CPHFlight flights[];

        public CPHFlight[] getFlights() {
            return flights;
        }

        public void setFlights(CPHFlight[] flights) {
            this.flights = flights;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CPHFlight {
            @JsonProperty("flt_no")
            private String flightNumber;
            @JsonProperty("scheduled")
            private String scheduled;
            @JsonProperty("origin")
            private String originName;
            @JsonProperty("origin-iata")
            private String originIATA;
            @JsonProperty("destination")
            private String destinationName;
            @JsonProperty("destination-iata")
            private String destinationIATA;
            @JsonProperty("airline")
            private String airline;
            @JsonProperty("estimate")
            private String estimate;
            @JsonProperty("gateno")
            private String gate;
            @JsonProperty("status")
            private void setStatus(Map<String, Object> delayAmount){
                status = (String) delayAmount.get("type");
            }
            private String status;

            public String getCheckinDetails() {
                return checkinDetails;
            }

            public void setCheckinDetails(String checkinDetails) {
                this.checkinDetails = checkinDetails;
            }

            @JsonProperty("checkin")
            private String checkinDetails;

            public String getFlightNumber() {
                return flightNumber;
            }

            public void setFlightNumber(String flightNumber) {
                this.flightNumber = flightNumber;
            }

            public String getScheduled() {
                return scheduled;
            }

            public void setScheduled(String scheduled) {
                this.scheduled = scheduled;
            }

            public String getOriginName() {
                return originName;
            }

            public void setOriginName(String originName) {
                this.originName = originName;
            }

            public String getOriginIATA() {
                return originIATA;
            }

            public void setOriginIATA(String originIATA) {
                this.originIATA = originIATA;
            }

            public String getDestinationName() {
                return destinationName;
            }

            public void setDestinationName(String destinationName) {
                this.destinationName = destinationName;
            }

            public String getDestinationIATA() {
                return destinationIATA;
            }

            public void setDestinationIATA(String destinationIATA) {
                this.destinationIATA = destinationIATA;
            }

            public String getAirline() {
                return airline;
            }

            public void setAirline(String airline) {
                this.airline = airline;
            }

            public String getEstimate() {
                return estimate;
            }

            public void setEstimate(String estimate) {
                this.estimate = estimate;
            }

            public String getGate() {
                return gate;
            }

            public void setGate(String gate) {
                this.gate = gate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
