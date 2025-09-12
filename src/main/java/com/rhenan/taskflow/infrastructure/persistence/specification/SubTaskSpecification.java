package com.rhenan.taskflow.infrastructure.persistence.specification;

import com.rhenan.taskflow.application.dto.request.SubTaskFilterRequest;
import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubTaskSpecification {
    
    public static Specification<SubTaskEntity> withFilters(SubTaskFilterRequest filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filters.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.status()));
            }
            
            if (filters.taskId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("taskId"), filters.taskId()));
            }
            
            if (filters.title() != null && !filters.title().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + filters.title().toLowerCase() + "%"
                ));
            }
            
            if (filters.createdAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("createdAt"), filters.createdAfter()
                ));
            }
            
            if (filters.createdBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("createdAt"), filters.createdBefore()
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}