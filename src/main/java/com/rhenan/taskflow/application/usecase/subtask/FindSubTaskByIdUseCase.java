package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.mapper.SubTaskMapper;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindSubTaskByIdUseCase {
    
    private final SubTaskRepository subTaskRepository;
    
    @Transactional(readOnly = true)
    public SubTaskResponse execute(UUID subTaskId) {
        SubTask subTask = subTaskRepository.findById(new SubTaskId(subTaskId))
            .orElseThrow(() -> new NotFoundException("Subtarefa não encontrada"));
        
        return SubTaskMapper.toResponse(subTask);
    }
}