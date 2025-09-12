package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.mapper.SubTaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSubTaskUseCase {
    
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    
    @Transactional
    public SubTaskResponse execute(CreateSubTaskRequest request) {
        TaskId taskId = new TaskId(request.taskId());
        
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Tarefa não encontrada");
        }
        
        SubTask newSubTask = SubTask.newSubTask(taskId, request.title(), request.description());
        SubTask savedSubTask = subTaskRepository.save(newSubTask);
        
        return SubTaskMapper.toResponse(savedSubTask);
    }
}