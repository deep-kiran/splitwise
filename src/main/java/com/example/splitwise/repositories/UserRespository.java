package com.example.splitwise.repositories;

import com.example.splitwise.models.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class UserRespository {

    HashMap<String, User> userHashMap =new HashMap<>();
    public void addUser(User user){
        if(user.getUserId()==null){
            user.setUserId(UUID.randomUUID().toString());
        }
        userHashMap.put(user.getUserId(),user);
    }
}
