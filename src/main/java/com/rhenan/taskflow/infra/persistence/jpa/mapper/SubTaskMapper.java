package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import org.springframework.stereotype.Component;

@Component
public class SubTaskMapper {

    public SubTaskEntity toEntity(SubTask subTask) {
        if (subTask == null) {
            return null;
        }
        
        SubTaskEntity entity = new SubTaskEntity();
        entity.setId(subTask.getId().value());
        entity.setTaskId(subTask.getTaskId().value());
        entity.setTitle(subTask.getTitle());
        entity.setDescription(subTask.getDescription());
        entity.setStatus(subTask.getStatus());
        entity.setCreatedAt(subTask.getCreatedAt());
        entity.setCompletedAt(subTask.getCompletedAt());
        return entity;
    }

    public SubTask toDomain(SubTaskEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return SubTask.newSubTask(
            new TaskId(entity.getTaskId()),
            entity.getTitle(),
            entity.getDescription()
        );
    }
}