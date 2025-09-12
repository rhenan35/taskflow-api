package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.request.CreateTaskRequest;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.application.mapper.TaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public TaskResponse execute(CreateTaskRequest request) {
        UserId userId = new UserId(request.userId());
        
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário não encontrado");
        }
        
        Task task = Task.createTask(userId, request.title(), request.description());
        
        Task savedTask = taskRepository.save(task);
        
        return TaskMapper.toResponse(savedTask);
    }
}