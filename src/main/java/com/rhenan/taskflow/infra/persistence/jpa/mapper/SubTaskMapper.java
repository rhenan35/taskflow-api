package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;
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
        entity.setTitle(subTask.getTitle().value());
        entity.setDescription(subTask.getDescription() != null ? subTask.getDescription().value() : null);
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
            new Title(entity.getTitle()),
            entity.getDescription() != null ? new Description(entity.getDescription()) : null
        );
    }
}