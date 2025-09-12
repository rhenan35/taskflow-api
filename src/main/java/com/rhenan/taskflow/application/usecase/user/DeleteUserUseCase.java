package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {
    
    private final UserRepository userRepository;
    
    @Transactional
    public void execute(UUID userId) {
        UserId userIdVO = new UserId(userId);
        
        if (!userRepository.existsById(userIdVO)) {
            throw new NotFoundException("Usuário não encontrado");
        }
        
        userRepository.deleteById(userIdVO);
    }
}