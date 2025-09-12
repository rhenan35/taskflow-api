package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.request.UpdateUserRequest;
import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.application.mapper.UserMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.Email;

import com.rhenan.taskflow.domain.valueObjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponse execute(UUID userId, UpdateUserRequest request) {
        User existingUser = userRepository.findById(new UserId(userId))
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        
        String newName = request.name() != null ? request.name() : existingUser.getName();
        Email newEmail = request.email() != null ? new Email(request.email()) : existingUser.getEmail();
        
        User newUser = User.newUser(newName, newEmail);
        User updatedUser = userRepository.save(newUser);
        
        return UserMapper.toResponse(updatedUser);
    }
}