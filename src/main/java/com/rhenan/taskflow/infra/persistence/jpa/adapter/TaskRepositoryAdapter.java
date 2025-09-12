package com.rhenan.taskflow.infra.persistence.jpa.adapter;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import com.rhenan.taskflow.infra.persistence.jpa.mapper.TaskMapper;
import com.rhenan.taskflow.infra.persistence.jpa.repository.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
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
}