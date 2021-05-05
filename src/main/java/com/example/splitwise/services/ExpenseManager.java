package com.example.splitwise.services;

import com.example.splitwise.exceptions.InvalidExpenseDataException;
import com.example.splitwise.exceptions.NoSuchExpenseTypeException;
import com.example.splitwise.models.*;
import com.example.splitwise.repositories.BalanceSheetRepository;
import com.example.splitwise.repositories.TransactionsRepository;
import com.example.splitwise.repositories.UserRespository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class ExpenseManager {


    @Autowired
    UserRespository userRespository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    BalanceSheetRepository balanceSheetRepository;

    public void createExpense(String activityId, List<ExactSplit> splitList, ExpenseType expenseType, double totalAmount) {
        double baseShare = totalAmount/splitList.size();
        for (int i=0;i<splitList.size();i++){
                ExactSplit split = splitList.get(i);
                double userShare = split.getAmountPaidByUser();
                double amountOwed = (baseShare - userShare);
                double amountOwedPerPerson =amountOwed/(splitList.size()-1);
                for(int j=0;j<splitList.size();j++){
                    ExactSplit split1 =splitList.get(j);
                    if(Objects.isNull(balanceSheetRepository.getBalance(split.getUserId(),split1.getUserId()))) {
                        BalanceSheet balanceSheet = BalanceSheet.builder()
                                .activityId(activityId)
                                .createdAt(new Date())
                                .updatedAt(new Date())
                                .amount(amountOwedPerPerson)
                                .build();
                        balanceSheetRepository.updateBalance(split.getUserId(), split1.getUserId(), balanceSheet);
                    }else {
                        BalanceSheet balanceSheet = balanceSheetRepository.getBalance(split.getUserId(), split1.getUserId());
                        balanceSheet.setAmount(balanceSheet.getAmount() + amountOwedPerPerson);
                        balanceSheetRepository.updateBalance(split.getUserId(),split1.getUserId(),balanceSheet);
                    }
                }
            }


    }


    public String showBalances(String userId){
            StringBuilder sb =new StringBuilder();
                HashMap<String,BalanceSheet> map  =balanceSheetRepository.getBalancesOfUser(userId);
                for(String userPaidTo : map.keySet()){
                    sb.append(userId).append("-->");
                    sb.append(userPaidTo).append("  balance of ").append(map.get(userPaidTo).getAmount());
                    sb.append("\n");
                }
                return sb.toString();
        }

        public String settle(String userId){
            StringBuilder sb =new StringBuilder();
//            Map<String,Double> map  =balanceSheet.get(userId);
//            for(String otherUserId : map.keySet()){
//                if(map.get(otherUserId)<0){   //userId owes otherUser balance
//                    generateString(sb,map,otherUserId,userId);
//                    Map<String,Double> map2 = balanceSheet.get(otherUserId);
//                    map2.put(userId,0.0);
//                    map.put(otherUserId,0.0);
//                    double balancedue =map.get(otherUserId);
//                    Transaction transaction =Transaction.builder()
//                            .userFrom(userId)
//                            .userTo(otherUserId)
//                            .amount(balancedue)
//                            .build();
//                    saveTransaction(transaction);
//                }else{                    //userId gains balance from other user
//                    generateString(sb,map,userId,otherUserId);
//                    Map<String,Double> map2 = balanceSheet.get(otherUserId);
//                    map2.put(userId,0.0);
//                    map.put(otherUserId,0.0);
//                    ArrayList<Split> splitArrayList =new ArrayList<>();
//                    double balancedue =map.get(otherUserId);
//                    Transaction transaction =Transaction.builder()
//                            .userFrom(otherUserId)
//                            .userTo(userId)
//                            .amount(balancedue)
//                            .build();
//                    saveTransaction(transaction);
//                }
//
//            }
            return sb.toString();
        }

    private void generateString(StringBuilder sb, Map<String, Double> map, String otherUser, String userId) {
        sb.append(userId).append("has paid ");
        sb.append(otherUser).append("  balance of ").append(Math.abs(map.get(otherUser)));
        sb.append("\n");
    }

    public void saveTransaction(Transaction transaction){
        transactionsRepository.addTransaction(transaction );
    }

}
