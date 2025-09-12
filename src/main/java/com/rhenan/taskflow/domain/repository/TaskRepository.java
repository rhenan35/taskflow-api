package com.rhenan.taskflow.domain.repository;

import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    
    Task save(Task task);
    
    Optional<Task> findById(TaskId taskId);
    
    List<Task> findByUserId(UserId userId);
    
    void deleteById(TaskId taskId);
    
    boolean existsById(TaskId taskId);
}