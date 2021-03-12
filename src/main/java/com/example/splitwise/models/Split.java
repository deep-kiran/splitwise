package com.example.splitwise.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Split {
    private User user;
    SplitType splitType;
    private double paidAmount;
}
