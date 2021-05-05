package com.example.splitwise.models;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSheet {
    String activityId;
    double amount;
    Date createdAt;
    Date updatedAt;
}
