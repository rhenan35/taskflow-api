package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.mapper.SubTaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSubTaskUseCase {
    
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    
    @Transactional
    public SubTaskResponse execute(CreateSubTaskRequest request) {
        TaskId taskId = new TaskId(request.taskId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        
        Title title = new Title(request.title());
        Description description = request.description() != null ? new Description(request.description()) : null;
        
        task.addSubTask(title, description);
        taskRepository.save(task);
        
        SubTask subTask = SubTask.newSubTask(taskId, title, description);
        SubTask savedSubTask = subTaskRepository.save(subTask);
        
        return SubTaskMapper.toResponse(savedSubTask);
    }
}