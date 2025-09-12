package com.rhenan.taskflow.infra.persistence.jpa.adapter;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import com.rhenan.taskflow.infra.persistence.jpa.mapper.TaskMapper;
import com.rhenan.taskflow.infra.persistence.jpa.repository.TaskJpaRepository;
import com.rhenan.taskflow.infra.persistence.jpa.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task save(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        TaskEntity savedEntity = taskJpaRepository.save(entity);
        return taskMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(TaskId taskId) {
        return taskJpaRepository.findById(taskId.value())
                .map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findByUserId(UserId userId) {
        return taskJpaRepository.findByUserId(userId.value())
                .stream()
                .map(taskMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByStatus(ActivityStatus status) {
        return taskJpaRepository.findByStatus(status)
                .stream()
                .map(taskMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(TaskId taskId) {
        taskJpaRepository.deleteById(taskId.value());
    }

    @Override
    public boolean existsById(TaskId taskId) {
        return taskJpaRepository.existsById(taskId.value());
    }
    
    @Override
    public PageResponse<Task> findWithFilters(TaskFilterRequest filters, PageRequest pageRequest) {
        Specification<TaskEntity> spec = TaskSpecification.withFilters(filters);
        
        Sort sort = createSort(pageRequest.sortBy(), pageRequest.sortDirection());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
            pageRequest.page(), 
            pageRequest.size(), 
            sort
        );
        
        Page<TaskEntity> entityPage = taskJpaRepository.findAll(spec, pageable);
        
        List<Task> tasks = entityPage.getContent()
            .stream()
            .map(taskMapper::toDomain)
            .toList();
        
        return PageResponse.of(
            tasks,
            entityPage.getNumber(),
            entityPage.getSize(),
            entityPage.getTotalElements()
        );
    }
    
    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        
        return Sort.by(direction, sortBy != null ? sortBy : "createdAt");
    }
}