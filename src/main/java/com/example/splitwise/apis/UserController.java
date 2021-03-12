package com.example.splitwise.apis;


import com.example.splitwise.exceptions.InvalidValuesException;
import com.example.splitwise.models.Expense;
import com.example.splitwise.models.User;
import com.example.splitwise.repositories.UserRespository;
import com.example.splitwise.services.ExpenseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRespository userRespository;

    @Autowired
    ExpenseManager expenseManager;

    @PostMapping("/create")
    public String createUser(@RequestBody User user){
        userRespository.addUser(user);
        return user.getUserId();
    }
    @GetMapping("/balance/{id}")
    public String usersBalance(@PathVariable (name = "id")String id){
         return expenseManager.showBalances(id);
    }

    @PostMapping("/addExpense")
    public void addExpense( Expense expense){
        validateExpense(expense);
        expenseManager.createExpense(expense.getSplitList(),expense.getExpenseType(),expense.getTotalAmount());
    }

    private void validateExpense(Expense expense) {
        if(expense.getExpenseType() ==null || expense.getSplitList().size()==0){
            throw new InvalidValuesException();
        }
    }
}
