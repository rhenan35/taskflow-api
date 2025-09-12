package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.application.mapper.TaskMapper;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTasksByStatusUseCase {
    
    private final TaskRepository taskRepository;
    
    @Transactional(readOnly = true)
    public List<TaskResponse> execute(ActivityStatus status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return tasks.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }
}