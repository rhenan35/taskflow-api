package com.rhenan.taskflow.infra.persistence.jpa.adapter;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.SubTaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import com.rhenan.taskflow.infra.persistence.jpa.mapper.SubTaskMapper;
import com.rhenan.taskflow.infra.persistence.jpa.repository.SubTaskJpaRepository;
import com.rhenan.taskflow.infrastructure.persistence.specification.SubTaskSpecification;
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
public class SubTaskRepositoryAdapter implements SubTaskRepository {

    private final SubTaskJpaRepository subTaskJpaRepository;
    private final SubTaskMapper subTaskMapper;

    @Override
    public SubTask save(SubTask subTask) {
        SubTaskEntity entity = subTaskMapper.toEntity(subTask);
        SubTaskEntity savedEntity = subTaskJpaRepository.save(entity);
        return subTaskMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<SubTask> findById(SubTaskId subTaskId) {
        return subTaskJpaRepository.findById(subTaskId.value())
                .map(subTaskMapper::toDomain);
    }

    @Override
    public List<SubTask> findByTaskId(TaskId taskId) {
        return subTaskJpaRepository.findByTaskId(taskId.value())
                .stream()
                .map(subTaskMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(SubTaskId subTaskId) {
        subTaskJpaRepository.deleteById(subTaskId.value());
    }

    @Override
    public void deleteByTaskId(TaskId taskId) {
        subTaskJpaRepository.deleteByTaskId(taskId.value());
    }

    @Override
    public boolean existsById(SubTaskId subTaskId) {
        return subTaskJpaRepository.existsById(subTaskId.value());
    }

    @Override
    public PageResponse<SubTask> findWithFilters(SubTaskFilterRequest filters, PageRequest pageRequest) {
        Specification<SubTaskEntity> spec = SubTaskSpecification.withFilters(filters);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(pageRequest.sortDirection()) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
            pageRequest.page(),
            pageRequest.size(),
            Sort.by(direction, pageRequest.sortBy())
        );
        
        Page<SubTaskEntity> page = subTaskJpaRepository.findAll(spec, pageable);
        
        List<SubTask> content = page.getContent()
            .stream()
            .map(subTaskMapper::toDomain)
            .toList();
        
        return PageResponse.of(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}