package com.rhenan.taskflow.infra.persistence.jpa.repository;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubTaskJpaRepository extends JpaRepository<SubTaskEntity, UUID> {
    
    List<SubTaskEntity> findByTaskId(UUID taskId);
    
    List<SubTaskEntity> findByTaskIdAndStatus(UUID taskId, ActivityStatus status);
    
    long countByTaskIdAndStatus(UUID taskId, ActivityStatus status);
    
    void deleteByTaskId(UUID taskId);
}