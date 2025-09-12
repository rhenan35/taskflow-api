package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.application.mapper.TaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTaskUseCase {
    
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    
    @Transactional(readOnly = true)
    public TaskResponse execute(UUID taskId) {
        TaskId taskIdVO = new TaskId(taskId);
        
        Task task = taskRepository.findById(taskIdVO)
            .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        
        List<SubTask> subTasks = subTaskRepository.findByTaskId(taskIdVO);
        
        return TaskMapper.toResponse(task, subTasks);
    }
}