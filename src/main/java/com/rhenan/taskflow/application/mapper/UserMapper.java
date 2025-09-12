package com.rhenan.taskflow.application.mapper;

import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.domain.model.User;

public class UserMapper {
    
    private UserMapper() {
    }
    
    public static UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId().value(),
            user.getName().value(),
            user.getEmail().value()
        );
    }
}