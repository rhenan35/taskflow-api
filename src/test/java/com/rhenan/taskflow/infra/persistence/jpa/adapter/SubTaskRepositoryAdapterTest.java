package com.rhenan.taskflow.infra.persistence.jpa.adapter;

import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.SubTaskEntity;
import com.rhenan.taskflow.infra.persistence.jpa.mapper.SubTaskMapper;
import com.rhenan.taskflow.infra.persistence.jpa.repository.SubTaskJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubTaskRepositoryAdapterTest {

    @Mock
    private SubTaskJpaRepository subTaskJpaRepository;
    
    @Mock
    private SubTaskMapper subTaskMapper;
    
    @InjectMocks
    private SubTaskRepositoryAdapter subTaskRepositoryAdapter;
    
    private SubTask subTask;
    private SubTaskEntity subTaskEntity;
    private SubTaskId subTaskId;
    private TaskId taskId;

    @BeforeEach
    void setUp() {
        subTaskId = new SubTaskId(UUID.randomUUID());
        taskId = new TaskId(UUID.randomUUID());
        subTask = SubTask.newSubTask(
            taskId,
            "SubTarefa Teste",
            "Descrição da subtarefa"
        );
        
        subTaskEntity = new SubTaskEntity();
        subTaskEntity.setId(subTaskId.value());
        subTaskEntity.setTaskId(taskId.value());
        subTaskEntity.setTitle("SubTarefa Teste");
        subTaskEntity.setDescription("Descrição da subtarefa");
    }

    @Test
    void deveSalvarSubTask() {
        when(subTaskMapper.toEntity(subTask)).thenReturn(subTaskEntity);
        when(subTaskJpaRepository.save(subTaskEntity)).thenReturn(subTaskEntity);
        when(subTaskMapper.toDomain(subTaskEntity)).thenReturn(subTask);

        SubTask resultado = subTaskRepositoryAdapter.save(subTask);

        assertNotNull(resultado);
        assertEquals(subTask, resultado);
        verify(subTaskMapper).toEntity(subTask);
        verify(subTaskJpaRepository).save(subTaskEntity);
        verify(subTaskMapper).toDomain(subTaskEntity);
    }

    @Test
    void deveBuscarSubTaskPorId() {
        when(subTaskJpaRepository.findById(subTaskId.value())).thenReturn(Optional.of(subTaskEntity));
        when(subTaskMapper.toDomain(subTaskEntity)).thenReturn(subTask);

        Optional<SubTask> resultado = subTaskRepositoryAdapter.findById(subTaskId);

        assertTrue(resultado.isPresent());
        assertEquals(subTask, resultado.get());
        verify(subTaskJpaRepository).findById(subTaskId.value());
        verify(subTaskMapper).toDomain(subTaskEntity);
    }

    @Test
    void deveRetornarVazioQuandoSubTaskNaoExistePorId() {
        when(subTaskJpaRepository.findById(subTaskId.value())).thenReturn(Optional.empty());

        Optional<SubTask> resultado = subTaskRepositoryAdapter.findById(subTaskId);

        assertFalse(resultado.isPresent());
        verify(subTaskJpaRepository).findById(subTaskId.value());
        verify(subTaskMapper, never()).toDomain(any());
    }

    @Test
    void deveBuscarSubTasksPorTaskId() {
        SubTaskEntity subTaskEntity2 = new SubTaskEntity();
        subTaskEntity2.setId(UUID.randomUUID());
        subTaskEntity2.setTaskId(taskId.value());
        
        SubTask subTask2 = SubTask.newSubTask(
            taskId,
            "Segunda SubTarefa",
            "Segunda descrição"
        );
        
        List<SubTaskEntity> entities = List.of(subTaskEntity, subTaskEntity2);
        
        when(subTaskJpaRepository.findByTaskId(taskId.value())).thenReturn(entities);
        when(subTaskMapper.toDomain(subTaskEntity)).thenReturn(subTask);
        when(subTaskMapper.toDomain(subTaskEntity2)).thenReturn(subTask2);

        List<SubTask> resultado = subTaskRepositoryAdapter.findByTaskId(taskId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(subTask, resultado.get(0));
        assertEquals(subTask2, resultado.get(1));
        verify(subTaskJpaRepository).findByTaskId(taskId.value());
        verify(subTaskMapper, times(2)).toDomain(any(SubTaskEntity.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaSubTasksParaTask() {
        when(subTaskJpaRepository.findByTaskId(taskId.value())).thenReturn(List.of());

        List<SubTask> resultado = subTaskRepositoryAdapter.findByTaskId(taskId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(subTaskJpaRepository).findByTaskId(taskId.value());
        verify(subTaskMapper, never()).toDomain(any());
    }

    @Test
    void deveDeletarSubTaskPorId() {
        subTaskRepositoryAdapter.deleteById(subTaskId);

        verify(subTaskJpaRepository).deleteById(subTaskId.value());
    }

    @Test
    void deveDeletarSubTasksPorTaskId() {
        subTaskRepositoryAdapter.deleteByTaskId(taskId);

        verify(subTaskJpaRepository).deleteByTaskId(taskId.value());
    }

    @Test
    void deveVerificarSeSubTaskExistePorId() {
        when(subTaskJpaRepository.existsById(subTaskId.value())).thenReturn(true);

        boolean resultado = subTaskRepositoryAdapter.existsById(subTaskId);

        assertTrue(resultado);
        verify(subTaskJpaRepository).existsById(subTaskId.value());
    }

    @Test
    void deveRetornarFalsoQuandoSubTaskNaoExistePorId() {
        when(subTaskJpaRepository.existsById(subTaskId.value())).thenReturn(false);

        boolean resultado = subTaskRepositoryAdapter.existsById(subTaskId);

        assertFalse(resultado);
        verify(subTaskJpaRepository).existsById(subTaskId.value());
    }
}