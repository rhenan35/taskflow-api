package com.rhenan.taskflow.infra.persistence.jpa.specification;

import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    
    public static Specification<TaskEntity> withFilters(TaskFilterRequest filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filters.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.status()));
            }
            
            if (filters.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), filters.userId()));
            }
            
            if (filters.title() != null && !filters.title().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), 
                    "%" + filters.title().toLowerCase() + "%"
                ));
            }
            
            if (filters.createdAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("createdAt"), 
                    filters.createdAfter().toInstant(ZoneOffset.UTC)
                ));
            }
            
            if (filters.createdBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("createdAt"), 
                    filters.createdBefore().toInstant(ZoneOffset.UTC)
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}