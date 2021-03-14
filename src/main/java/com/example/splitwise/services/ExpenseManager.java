package com.example.splitwise.services;

import com.example.splitwise.exceptions.InvalidExpenseDataException;
import com.example.splitwise.exceptions.NoSuchExpenseTypeException;
import com.example.splitwise.models.*;
import com.example.splitwise.repositories.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class ExpenseManager {


    @Autowired
    UserRespository userRespository;

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

            if(expenseType==ExpenseType.EXACT){
                calculateBalancesExact(expense);
            }else{
                calculateBalancesPercentage(expense);
            }

        }

    private void calculateBalancesPercentage(Expense expense) {
        List<PercentageSplit> paidByUsersList = new ArrayList<>();
        List<PercentageSplit> paidToUsersList = new ArrayList<>();
        expense.getSplitList().forEach(split -> {
            PercentageSplit percentageSplit = (PercentageSplit) split;
            if (percentageSplit.getPercentageShare() > 0) {
                paidByUsersList.add(percentageSplit);
            }else
                paidToUsersList.add(percentageSplit);
        });

        for (PercentageSplit paidByUser : paidByUsersList) {
            double baseAmount = paidByUser.getPercentageShare()*expense.getTotalAmount() /paidToUsersList.size();
            balanceSheet.put(paidByUser.getUser().getUserId(), new HashMap<>());
            for (PercentageSplit paidToUsers : paidToUsersList) {
                String paidTo = paidToUsers.getUser().getUserId();
                Map<String, Double> balanceOfUser = balanceSheet.get(paidByUser);
                if (!balanceOfUser.containsKey(paidTo)) {
                    balanceOfUser.put(paidTo, 0.0);
                }
                balanceOfUser.put(paidTo, balanceOfUser.get(paidTo) + baseAmount);

                balanceOfUser = balanceSheet.get(paidTo);
                if (!balanceOfUser.containsKey(paidByUser.getUser().getUserId())) {
                    balanceOfUser.put(paidByUser.getUser().getUserId(), 0.0);
                }
                balanceOfUser.put(paidByUser.getUser().getUserId(), balanceOfUser.get(paidByUser) - baseAmount);
            }
        }
    }

    private void calculateBalancesExact(Expense expense) {
        List<ExactSplit> paidByUsersList = new ArrayList<>();
        List<ExactSplit> paidToUsersList = new ArrayList<>();
        expense.getSplitList().forEach(split -> {
            ExactSplit exactSplit = (ExactSplit) split;
            if (exactSplit.getAmountPaidByUser() > 0) {
                paidByUsersList.add(exactSplit);
            }else
                paidToUsersList.add(exactSplit);
        });

        for (ExactSplit paidByUser : paidByUsersList) {
            double baseAmount = paidByUser.getAmountPaidByUser()/paidToUsersList.size();
            balanceSheet.put(paidByUser.getUser().getUserId(), new HashMap<>());
            for (ExactSplit paidToUsers : paidToUsersList) {
                String paidTo = paidToUsers.getUser().getUserId();
                Map<String, Double> balanceOfUser = balanceSheet.get(paidByUser);
                if (!balanceOfUser.containsKey(paidTo)) {
                    balanceOfUser.put(paidTo, 0.0);
                }
                balanceOfUser.put(paidTo, balanceOfUser.get(paidTo) + baseAmount);

                balanceOfUser = balanceSheet.get(paidTo);
                if (!balanceOfUser.containsKey(paidByUser.getUser().getUserId())) {
                    balanceOfUser.put(paidByUser.getUser().getUserId(), 0.0);
                }
                balanceOfUser.put(paidByUser.getUser().getUserId(), balanceOfUser.get(paidByUser) - baseAmount);
            }
        }
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
                    generateString(sb,map,otherUser,userId);
                    Map<String,Double> map2 = balanceSheet.get(otherUser);
                    map2.put(userId,0.0);
                    ArrayList<Split> splitArrayList =new ArrayList<>();
                    double balancedue =map.get(otherUser);
                    splitArrayList.add(new ExactSplit(userRespository.getUserById(userId),balancedue));
                    splitArrayList.add(new ExactSplit(userRespository.getUserById(otherUser),0));
                    expenses.add(new Expense(ExpenseType.EXACT,splitArrayList,map.get(otherUser)));
                }

            }
            return sb.toString();
        }

    private void generateString(StringBuilder sb, Map<String, Double> map, String otherUser, String userId) {
        sb.append(userId).append("has paid ");
        sb.append(otherUser).append("  balance of ").append(Math.abs(map.get(otherUser)));
        sb.append("\n");
    }

}
