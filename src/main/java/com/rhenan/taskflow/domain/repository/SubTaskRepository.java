package com.rhenan.taskflow.domain.repository;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.SubTaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;

import java.util.List;
import java.util.Optional;

public interface SubTaskRepository {
    
    SubTask save(SubTask subTask);
    
    Optional<SubTask> findById(SubTaskId subTaskId);
    
    List<SubTask> findByTaskId(TaskId taskId);
    
    PageResponse<SubTask> findWithFilters(SubTaskFilterRequest filters, PageRequest pageRequest);
    
    void deleteById(SubTaskId subTaskId);
    
    void deleteByTaskId(TaskId taskId);
    
    boolean existsById(SubTaskId subTaskId);
}