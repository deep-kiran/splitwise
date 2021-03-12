package com.example.splitwise.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExactSplit extends Split{
    double amountPaidByUser;
    public ExactSplit(User user, SplitType splitType, double amountPaidByUser) {
        super(user, SplitType.EXACT);
        this.amountPaidByUser =amountPaidByUser;
    }
}
