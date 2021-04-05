package com.example.splitwise.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExactSplit extends Split{
    double amountPaidByUser;
    public ExactSplit(String userId,  double amountPaidByUser) {
        super(userId, SplitType.EXACT);
        this.amountPaidByUser =amountPaidByUser;
    }
}
