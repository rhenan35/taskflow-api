package com.rhenan.taskflow.infra.persistence.jpa.repository;

import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubTaskJpaRepository extends JpaRepository<SubTaskEntity, UUID>, JpaSpecificationExecutor<SubTaskEntity> {
    
    List<SubTaskEntity> findByTaskId(UUID taskId);
    

    
    void deleteByTaskId(UUID taskId);
}