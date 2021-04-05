package com.example.splitwise.repositories;

import com.example.splitwise.models.Activity;
import com.example.splitwise.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class ActivityRepository {
    HashMap<String, Activity> activityHashMap =new HashMap<>();
    public String addActivity(Activity activity){
        if(activity.getId()==null){
            activity.setId(UUID.randomUUID().toString());
        }
        activityHashMap.put(activity.getId(),activity);
        return activity.getId();
    }

    public Activity getActivityById(String id){
        return activityHashMap.get(id);
    }

}
