package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.application.mapper.TaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTaskByIdUseCase {
    
    private final TaskRepository taskRepository;
    
    @Transactional(readOnly = true)
    public TaskResponse execute(UUID taskId) {
        Task task = taskRepository.findById(new TaskId(taskId))
            .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        
        return TaskMapper.toResponse(task);
    }
}