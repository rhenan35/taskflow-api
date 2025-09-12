package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.mapper.SubTaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSubTaskUseCase {
    
    private final TaskRepository taskRepository;
    
    @Transactional
    public SubTaskResponse execute(CreateSubTaskRequest request) {
        TaskId taskId = new TaskId(request.taskId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        
        task.addSubTask(request.title(), request.description());
        Task savedTask = taskRepository.save(task);
        
        SubTask createdSubTask = savedTask.getSubTask().get(savedTask.getSubTask().size() - 1);
        
        return SubTaskMapper.toResponse(createdSubTask);
    }
}