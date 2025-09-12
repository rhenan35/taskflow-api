package com.rhenan.taskflow.domain.factory;

import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.Name;

public class UserFactory {
    
    private UserFactory() {}
    
    public static User createUser(String name, String email) {
        Name userName = new Name(name);
        Email userEmail = new Email(email);
        
        return User.newUser(userName, userEmail);
    }
}