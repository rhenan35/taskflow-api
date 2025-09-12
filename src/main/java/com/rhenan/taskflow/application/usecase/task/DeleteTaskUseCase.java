package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCase {
    
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    
    @Transactional
    public void execute(UUID taskId) {
        TaskId taskIdVO = new TaskId(taskId);
        
        if (!taskRepository.existsById(taskIdVO)) {
            throw new NotFoundException("Tarefa não encontrada");
        }
        
        subTaskRepository.deleteByTaskId(taskIdVO);
        taskRepository.deleteById(taskIdVO);
    }
}