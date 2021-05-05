package com.example.splitwise.repositories;


import com.example.splitwise.models.BalanceSheet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BalanceSheetRepository {

    HashMap<String, HashMap<String, BalanceSheet>> balanceSheetmap =new HashMap<>();
    public void updateBalance(String user1, String user2, BalanceSheet balanceSheet){
        balanceSheetmap.get(user1).put(user2,balanceSheet);

    }

    public BalanceSheet getBalance(String userId, String userId1) {
        return balanceSheetmap.get(userId).get(userId1);
    }

    public HashMap<String, BalanceSheet> getBalancesOfUser(String userId) {
        return balanceSheetmap.get(userId);
    }
}
