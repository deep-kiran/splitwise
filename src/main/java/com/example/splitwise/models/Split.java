package com.example.splitwise.models;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Split {
    private String userId;
    private SplitType splitType;
    String activityName;

    @JsonUnwrapped
    ExactSplit exactSplit;

    @JsonUnwrapped
    PercentageSplit percentageSplit;


    public Split(String userId, SplitType splitType) {
        this.userId = userId;
        this.splitType =splitType;
    }
}
