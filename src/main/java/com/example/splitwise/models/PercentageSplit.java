package com.example.splitwise.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentageSplit extends Split{
    double percentageShare;
    public PercentageSplit(User user, SplitType splitType, double percentageShare) {
        super(user, SplitType.PERCENTAGE);
        this.percentageShare =percentageShare;
    }
}
