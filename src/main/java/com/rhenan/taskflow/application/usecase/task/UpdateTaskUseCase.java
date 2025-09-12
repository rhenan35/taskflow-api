package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.request.UpdateTaskRequest;
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
public class UpdateTaskUseCase {
    
    private final TaskRepository taskRepository;
    
    @Transactional
    public TaskResponse execute(UUID taskId, UpdateTaskRequest request) {
        Task existingTask = taskRepository.findById(new TaskId(taskId))
            .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        
        if (request.title() != null) {
            existingTask.updateTitle(request.title());
        }
        
        if (request.description() != null) {
            existingTask.updateDescription(request.description());
        }
        
        Task updatedTask = taskRepository.save(existingTask);
        
        return TaskMapper.toResponse(updatedTask);
    }
}