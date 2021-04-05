package com.example.splitwise.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentageSplit extends Split{
    private double percentageShare;
    public PercentageSplit(String user, SplitType splitType, double percentageShare) {
        super(user, SplitType.PERCENTAGE);
        this.percentageShare =percentageShare;
    }
}
