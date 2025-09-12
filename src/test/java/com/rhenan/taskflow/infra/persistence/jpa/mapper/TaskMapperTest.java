package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    void deveConverterTaskParaEntity() {
        UserId userId = new UserId(UUID.randomUUID());
        Task task = Task.createTask(
            userId,
            "Tarefa Teste",
            "Descrição da tarefa"
        );

        TaskEntity entity = taskMapper.toEntity(task);

        assertNotNull(entity);
        assertEquals(task.getId().value(), entity.getId());
        assertEquals("Tarefa Teste", entity.getTitle());
        assertEquals("Descrição da tarefa", entity.getDescription());
        assertEquals(ActivityStatus.PENDING, entity.getStatus());
        assertEquals(userId.value(), entity.getUserId());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void deveRetornarNullQuandoTaskForNull() {
        TaskEntity entity = taskMapper.toEntity(null);
        
        assertNull(entity);
    }

    @Test
    void deveConverterEntityParaTask() {
        TaskEntity entity = new TaskEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId(UUID.randomUUID());
        entity.setTitle("Task Entity");
        entity.setDescription("Descrição entity");
        entity.setStatus(ActivityStatus.IN_PROGRESS);
        entity.setCreatedAt(Instant.now());

        Task task = taskMapper.toDomain(entity);

        assertNotNull(task);
        assertEquals("Task Entity", task.getTitle());
        assertEquals("Descrição entity", task.getDescription());
        assertEquals(entity.getUserId(), task.getUserId().value());
    }

    @Test
    void deveRetornarNullQuandoEntityForNull() {
        Task task = taskMapper.toDomain(null);
        
        assertNull(task);
    }

    @Test
    void deveManterConsistenciaEntreConversoes() {
        UserId userId = new UserId(UUID.randomUUID());
        Task originalTask = Task.createTask(
            userId,
            "Consistência",
            "Teste de consistência"
        );

        TaskEntity entity = taskMapper.toEntity(originalTask);
        Task convertedTask = taskMapper.toDomain(entity);

        assertEquals(originalTask.getTitle(), convertedTask.getTitle());
        assertEquals(originalTask.getDescription(), convertedTask.getDescription());
        assertEquals(originalTask.getUserId().value(), convertedTask.getUserId().value());
    }
}