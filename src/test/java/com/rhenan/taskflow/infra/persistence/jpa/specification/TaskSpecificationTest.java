package com.rhenan.taskflow.infra.persistence.jpa.specification;

import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskSpecificationTest {

    @Mock
    private Root<TaskEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> path;

    @Mock
    private Predicate predicate;

    @Test
    void deveAplicarFiltroDeStatus() {
        TaskFilterRequest filters = TaskFilterRequest.builder()
                .status(ActivityStatus.PENDING)
                .build();

        when(root.get("status")).thenReturn(path);
        when(criteriaBuilder.equal(path, ActivityStatus.PENDING)).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        Specification<TaskEntity> spec = TaskSpecification.withFilters(filters);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(path, ActivityStatus.PENDING);
    }

    @Test
    void deveAplicarFiltroDeUserId() {
        UUID userId = UUID.randomUUID();
        TaskFilterRequest filters = TaskFilterRequest.builder()
                .userId(userId)
                .build();

        when(root.get("userId")).thenReturn(path);
        when(criteriaBuilder.equal(path, userId)).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        Specification<TaskEntity> spec = TaskSpecification.withFilters(filters);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(path, userId);
    }

    @Test
    void deveIgnorarTituloVazio() {
        TaskFilterRequest filters = TaskFilterRequest.builder()
                .title("   ")
                .build();

        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        Specification<TaskEntity> spec = TaskSpecification.withFilters(filters);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).and(any(Predicate[].class));
    }
}