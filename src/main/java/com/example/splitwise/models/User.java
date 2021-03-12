package com.example.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class User {
    private String userId;
    private String name;
    private String password;
    private List<Expense> expenseList;


}
