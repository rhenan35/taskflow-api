package com.rhenan.taskflow.application.mapper;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.model.SubTask;

public class SubTaskMapper {
    
    private SubTaskMapper() {
    }
    
    public static SubTaskResponse toResponse(SubTask subTask) {
        return new SubTaskResponse(
            subTask.getId().value(),
            subTask.getTaskId().value(),
            subTask.getTitle(),
            subTask.getDescription(),
            subTask.getStatus(),
            subTask.getCreatedAt(),
            subTask.getCompletedAt()
        );
    }
}