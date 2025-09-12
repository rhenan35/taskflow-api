package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.application.mapper.TaskMapper;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindTasksWithFiltersUseCase {
    
    private final TaskRepository taskRepository;
    
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> execute(TaskFilterRequest filters, PageRequest pageRequest) {
        PageResponse<Task> taskPage = taskRepository.findWithFilters(filters, pageRequest);
        
        return PageResponse.of(
            taskPage.content().stream()
                .map(TaskMapper::toResponse)
                .toList(),
            taskPage.page(),
            taskPage.size(),
            taskPage.totalElements()
        );
    }
}