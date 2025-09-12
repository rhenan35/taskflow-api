package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.valueObjects.*;
import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskMapperTest {

    private SubTaskMapper subTaskMapper;

    @BeforeEach
    void setUp() {
        subTaskMapper = new SubTaskMapper();
    }

    @Test
    void deveConverterSubTaskParaEntity() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        SubTask subTask = SubTask.newSubTask(
            taskId,
            new Title("SubTarefa Teste"),
            new Description("Descrição da subtarefa")
        );

        SubTaskEntity entity = subTaskMapper.toEntity(subTask);

        assertNotNull(entity);
        assertEquals(subTask.getId().value(), entity.getId());
        assertEquals("SubTarefa Teste", entity.getTitle());
        assertEquals("Descrição da subtarefa", entity.getDescription());
        assertEquals(ActivityStatus.PENDING, entity.getStatus());
        assertEquals(taskId.value(), entity.getTaskId());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void deveRetornarNullQuandoSubTaskForNull() {
        SubTaskEntity entity = subTaskMapper.toEntity(null);
        
        assertNull(entity);
    }

    @Test
    void deveConverterEntityParaSubTask() {
        SubTaskEntity entity = new SubTaskEntity();
        entity.setId(UUID.randomUUID());
        entity.setTaskId(UUID.randomUUID());
        entity.setTitle("SubTask Entity");
        entity.setDescription("Descrição entity");
        entity.setStatus(ActivityStatus.IN_PROGRESS);
        entity.setCreatedAt(Instant.now());

        SubTask subTask = subTaskMapper.toDomain(entity);

        assertNotNull(subTask);
        assertEquals("SubTask Entity", subTask.getTitle().value());
        assertEquals("Descrição entity", subTask.getDescription().value());
        assertEquals(entity.getTaskId(), subTask.getTaskId().value());
    }

    @Test
    void deveRetornarNullQuandoEntityForNull() {
        SubTask subTask = subTaskMapper.toDomain(null);
        
        assertNull(subTask);
    }

    @Test
    void deveConverterSubTaskComDescricaoNula() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        SubTask subTask = SubTask.newSubTask(
            taskId,
            new Title("Título sem descrição"),
            null
        );

        SubTaskEntity entity = subTaskMapper.toEntity(subTask);

        assertNotNull(entity);
        assertEquals("Título sem descrição", entity.getTitle());
        assertNull(entity.getDescription());
    }

    @Test
    void deveManterConsistenciaEntreConversoes() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        SubTask originalSubTask = SubTask.newSubTask(
            taskId,
            new Title("Consistência SubTask"),
            new Description("Teste de consistência")
        );

        SubTaskEntity entity = subTaskMapper.toEntity(originalSubTask);
        SubTask convertedSubTask = subTaskMapper.toDomain(entity);

        assertEquals(originalSubTask.getTitle().value(), convertedSubTask.getTitle().value());
        assertEquals(originalSubTask.getDescription().value(), convertedSubTask.getDescription().value());
        assertEquals(originalSubTask.getTaskId().value(), convertedSubTask.getTaskId().value());
    }
}