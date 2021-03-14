package com.example.splitwise.services;

import com.example.splitwise.exceptions.InvalidExpenseDataException;
import com.example.splitwise.exceptions.NoSuchExpenseTypeException;
import com.example.splitwise.models.*;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class ExpenseManager {
    ArrayList<Expense> expenses =new ArrayList<>();
    HashMap<String, Map<String,Double>> balanceSheet =new HashMap<>(); //userId --> userId balance

    public Split getType(Split split){
        if(split.getSplitType()==SplitType.EXACT){
            return (ExactSplit) split;
        }
        return (PercentageSplit)split;
    }


        public void createExpense(List<Split> splitList, ExpenseType expenseType, double totalAmount) {
            Expense expense;
            switch (expenseType) {
                case EXACT:
                    expense = new Expense(expenseType.EXACT, splitList, totalAmount);
                    if(!expense.validateExactExpense()){
                        throw new InvalidExpenseDataException();
                    };
                    break;
                case PERCENTAGE:
                    expense = new Expense(expenseType.PERCENTAGE, splitList, totalAmount);
                    if(!expense.validatePercentageExpense()){
                        throw new InvalidExpenseDataException();
                    };
                    break;
                default:
                    throw new NoSuchExpenseTypeException();
            }
            expenses.add(expense);
            List<Split> paidByUsersList = new ArrayList<>();
            List<Split> paidToUsersList = new ArrayList<>();

            expense.getSplitList().forEach(split -> {
                switch (split.getSplitType()) {
                    case PERCENTAGE:
                        PercentageSplit percentageSplit = (PercentageSplit) split;
                        if (percentageSplit.getPercentageShare() > 0)
                            paidByUsersList.add(percentageSplit);
                        else
                            paidToUsersList.add(percentageSplit);

                    case EXACT:
                        ExactSplit exactSplit = (ExactSplit) split;
                        if (exactSplit.getAmountPaidByUser() > 0) {
                            paidByUsersList.add(exactSplit);
                        }else
                            paidToUsersList.add(exactSplit);
                }
            });

//            for (Split paidByUser : paidByUsersList) {
//                double baseAmount = paidByUser.getPaidAmount() / paidToUsersList.size();
//                balanceSheet.put(paidByUser.getUser().getUserId(), new HashMap<>());
//                for (Split paidToUsers : paidToUsersList) {
//                    String paidTo = paidToUsers.getUser().getUserId();
//                    Map<String, Double> balanceOfUser = balanceSheet.get(paidByUser);
//                    if (!balanceOfUser.containsKey(paidTo)) {
//                        balanceOfUser.put(paidTo, 0.0);
//                    }
//                    balanceOfUser.put(paidTo, balanceOfUser.get(paidTo) + baseAmount);
//
//                    balanceOfUser = balanceSheet.get(paidTo);
//                    if (!balanceOfUser.containsKey(paidByUser.getUser().getUserId())) {
//                        balanceOfUser.put(paidByUser.getUser().getUserId(), 0.0);
//                    }
//                    balanceOfUser.put(paidByUser.getUser().getUserId(), balanceOfUser.get(paidByUser) - baseAmount);
//                }
//            }
        }

        public String showBalances(String userId){
            StringBuilder sb =new StringBuilder();
                Map<String,Double> map  =balanceSheet.get(userId);
                for(String userPaidTo : map.keySet()){
                    sb.append(userId).append("-->");
                    sb.append(userPaidTo).append("  balance of ").append(map.get(userPaidTo));
                    sb.append("\n");
                }
                return sb.toString();
        }

        public String settle(String userId){
            StringBuilder sb =new StringBuilder();
            Map<String,Double> map  =balanceSheet.get(userId);
            for(String otherUser : map.keySet()){
                if(map.get(otherUser)<0){
                    sb.append(userId).append("has paid ");
                    sb.append(otherUser).append("  balance of ").append(Math.abs(map.get(otherUser)));
                    Map<String,Double> map2 = balanceSheet.get(otherUser);
                    map2.put(userId,0.0);
                    sb.append("\n");
                }

            }
            return sb.toString();
        }

}
