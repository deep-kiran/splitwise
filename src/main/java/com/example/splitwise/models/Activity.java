package com.example.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    public boolean validateExactExpense(){
        double sum =0;
        for(Split split :splitList){
            if(!(split instanceof ExactSplit)){
                return false;
            }
            ExactSplit exactSplit =(ExactSplit)split;
            sum +=exactSplit.getAmountPaidByUser();
        }
        return sum==totalAmount;
    }
    public boolean validatePercentageExpense(){
        double percentage =0;
        for(Split split : splitList){
            if(!(split instanceof PercentageSplit)){
                return false;
            }
            PercentageSplit percentageSplit =(PercentageSplit)split;
            percentage += percentageSplit.getPercentageShare();
        }
        return percentage ==100;
    }

}
