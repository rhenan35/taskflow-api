package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.UpdateSubTaskRequest;
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
public class UpdateSubTaskUseCase {
    
    private final SubTaskRepository subTaskRepository;
    
    @Transactional
    public SubTaskResponse execute(UUID subTaskId, UpdateSubTaskRequest request) {
        SubTask existingSubTask = subTaskRepository.findById(new SubTaskId(subTaskId))
            .orElseThrow(() -> new NotFoundException("Subtarefa não encontrada"));
        
        String newTitle = request.title() != null ? request.title() : existingSubTask.getTitle();
        String newDescription = request.description() != null ? request.description() : existingSubTask.getDescription();
        
        SubTask updatedSubTask = SubTask.newSubTask(
            existingSubTask.getTaskId(),
            newTitle,
            newDescription
        );
        
        updatedSubTask = subTaskRepository.save(updatedSubTask);
        
        return SubTaskMapper.toResponse(updatedSubTask);
    }
}