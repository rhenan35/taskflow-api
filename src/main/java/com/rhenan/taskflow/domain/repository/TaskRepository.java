package com.rhenan.taskflow.domain.repository;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    
    Task save(Task task);
    
    Optional<Task> findById(TaskId taskId);
    
    List<Task> findByUserId(UserId userId);
    
    List<Task> findByStatus(ActivityStatus status);
    
    PageResponse<Task> findWithFilters(TaskFilterRequest filters, PageRequest pageRequest);
    
    void deleteById(TaskId taskId);
    
    boolean existsById(TaskId taskId);
}