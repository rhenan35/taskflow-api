package com.rhenan.taskflow.application.mapper;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.model.Task;

import java.util.List;

public class TaskMapper {
    
    private TaskMapper() {
    }
    
    public static TaskResponse toResponse(Task task) {
        return toResponse(task, task.getSubTask());
    }
    
    public static TaskResponse toResponse(Task task, List<SubTask> subTasks) {
        List<SubTaskResponse> subTaskResponses = subTasks != null ? 
            subTasks.stream().map(SubTaskMapper::toResponse).toList() : 
            List.of();
            
        return new TaskResponse(
            task.getId().value(),
            task.getUserId().value(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreatedAt(),
            task.getCompletedAt(),
            subTaskResponses
        );
    }
}