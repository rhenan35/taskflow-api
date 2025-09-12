package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.request.CreateUserRequest;
import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.application.mapper.UserMapper;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponse execute(CreateUserRequest request) {
        Email userEmail = new Email(request.email());
        User user = User.newUser(request.name(), userEmail);
        
        User savedUser = userRepository.save(user);
        
        return UserMapper.toResponse(savedUser);
    }
}