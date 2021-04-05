package com.example.splitwise.apis;


import com.example.splitwise.exceptions.InvalidValuesException;
import com.example.splitwise.models.Activity;
import com.example.splitwise.models.Split;
import com.example.splitwise.models.User;
import com.example.splitwise.repositories.ActivityRepository;
import com.example.splitwise.repositories.UserRespository;
import com.example.splitwise.services.ExpenseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserRespository userRespository;

    @Autowired
    ExpenseManager expenseManager;

    @Autowired
    ActivityRepository activityRepository;

    @PostMapping("/create")
    public String createUser(@RequestBody User user){
        validateUser(user);
        userRespository.addUser(user);
        return user.getUserId();
    }

    private void validateUser(User user) {
        if (user==null || user.getName()==null ){
            throw new InvalidValuesException("Values not present");
        }
    }

    @GetMapping("/balance/{id}")
    public String usersBalance(@PathVariable (name = "id")String id){
         return expenseManager.showBalances(id);
    }

    @PostMapping("/addExpense")
    public void addExpense( List<Split> splitList){
        String activityId = splitList.get(0).getActivityId();
        Activity activity = activityRepository.getActivityById(activityId);
        activity.getSplitList().addAll(splitList);
        activityRepository.addActivity(activity);
    }

    @PostMapping("/settle/{userId}")
    public String settle(@PathVariable (name = "userId")String userId){
        return expenseManager.settle(userId);
    }

    @PostMapping("/createActivity")
    public String createActivity(@RequestBody Activity activity){
        return activityRepository.addActivity(activity);
    }


    @GetMapping("/generateTimeLine/{activityId}")
    public List<String> getTransactions(@PathVariable (name ="activityId") String activityId){
        return activityRepository.getActivityById(activityId).getTransactions().stream().map(transaction -> {
            return transaction.toString();
        }).collect(Collectors.toList());
    }
}
