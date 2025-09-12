package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteSubTaskUseCase {
    
    private final SubTaskRepository subTaskRepository;
    
    @Transactional
    public void execute(UUID subTaskId) {
        SubTaskId subTaskIdVO = new SubTaskId(subTaskId);
        
        if (!subTaskRepository.existsById(subTaskIdVO)) {
            throw new NotFoundException("Subtarefa não encontrada");
        }
        
        subTaskRepository.deleteById(subTaskIdVO);
    }
}