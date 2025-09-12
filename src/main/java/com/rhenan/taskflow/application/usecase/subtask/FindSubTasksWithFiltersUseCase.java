package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.SubTaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.mapper.SubTaskMapper;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindSubTasksWithFiltersUseCase {
    
    private final SubTaskRepository subTaskRepository;
    
    @Transactional(readOnly = true)
    public PageResponse<SubTaskResponse> execute(SubTaskFilterRequest filters, PageRequest pageRequest) {
        PageResponse<SubTask> subTaskPage = subTaskRepository.findWithFilters(filters, pageRequest);
        
        return PageResponse.of(
            subTaskPage.content().stream()
                .map(SubTaskMapper::toResponse)
                .toList(),
            subTaskPage.page(),
            subTaskPage.size(),
            subTaskPage.totalElements()
        );
    }
}