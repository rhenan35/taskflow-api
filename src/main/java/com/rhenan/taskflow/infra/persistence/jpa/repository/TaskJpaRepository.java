package com.rhenan.taskflow.infra.persistence.jpa.repository;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {
    
    List<TaskEntity> findByUserId(UUID userId);
    
    List<TaskEntity> findByStatus(ActivityStatus status);
}