package com.example.splitwise.repositories;

import com.example.splitwise.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class TransactionsRepository {
    HashMap<String, Transaction> transactionHashMap =new HashMap<>();
    public void addTransaction(Transaction transaction) {
        if(transaction.getTransactionId()==null){
            transaction.setTransactionId(UUID.randomUUID().toString());
        }
        transactionHashMap.put(transaction.getTransactionId(),transaction);
    }
}
