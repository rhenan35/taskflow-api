package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.application.mapper.UserMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserUseCase {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public UserResponse execute(UUID userId) {
        User user = userRepository.findById(new UserId(userId))
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        
        return UserMapper.toResponse(user);
    }
}