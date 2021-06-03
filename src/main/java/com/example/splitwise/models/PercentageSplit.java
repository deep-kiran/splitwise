package com.example.splitwise.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentageSplit {
    @JsonProperty("percentage_share")
    private double percentageShare;

}
