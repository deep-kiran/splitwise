package com.example.splitwise.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private String id;
    private String activityName;
    private String description;
    private List<Split> splitList =new ArrayList<>();
    private ExpenseType expenseType;
    private double totalAmount;
    private List<Transaction> transactions =new ArrayList<>();


    public Activity( ExpenseType expenseType, List<Split> splitList, double totalAmount) {
        this.id = UUID.randomUUID().toString();
        this.splitList =splitList;
        this.expenseType =expenseType;
        this.totalAmount =totalAmount;
    }


}
