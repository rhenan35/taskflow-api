package com.rhenan.taskflow.application.usecase.subtask;

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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindSubTasksByTaskIdUseCase {
    
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    
    @Transactional(readOnly = true)
    public List<SubTaskResponse> execute(UUID taskId) {
        TaskId taskIdVO = new TaskId(taskId);
        
        if (!taskRepository.existsById(taskIdVO)) {
            throw new NotFoundException("Tarefa não encontrada");
        }
        
        List<SubTask> subTasks = subTaskRepository.findByTaskId(taskIdVO);
        return subTasks.stream()
                .map(SubTaskMapper::toResponse)
                .toList();
    }
}