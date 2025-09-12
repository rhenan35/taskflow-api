package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import com.rhenan.taskflow.infra.persistence.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity();
        entity.setId(user.getId().value());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail().value());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return User.fromExisting(
            new UserId(entity.getId()),
            entity.getName(),
            new Email(entity.getEmail())
        );
    }
}