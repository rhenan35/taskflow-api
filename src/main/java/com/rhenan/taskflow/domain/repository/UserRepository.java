package com.rhenan.taskflow.domain.repository;

import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.util.Optional;

public interface UserRepository {
    
    User save(User user);
    
    Optional<User> findById(UserId userId);
    
    Optional<User> findByEmail(Email email);
    
    void deleteById(UserId userId);
    
    boolean existsById(UserId userId);
    
    boolean existsByEmail(Email email);
}