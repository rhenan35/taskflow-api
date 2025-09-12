package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.model.Task;

import com.rhenan.taskflow.domain.valueObjects.UserId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId().value());
        entity.setUserId(task.getUserId().value());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setStatus(task.getStatus());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setCompletedAt(task.getCompletedAt());
        return entity;
    }

    public Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Task.createTask(
            new UserId(entity.getUserId()),
            entity.getTitle(),
            entity.getDescription()
        );
    }
}