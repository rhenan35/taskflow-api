package com.rhenan.taskflow.infra.persistence.jpa.adapter;

import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.TaskEntity;
import com.rhenan.taskflow.infra.persistence.jpa.mapper.TaskMapper;
import com.rhenan.taskflow.infra.persistence.jpa.repository.TaskJpaRepository;
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
class TaskRepositoryAdapterTest {

    @Mock
    private TaskJpaRepository taskJpaRepository;
    
    @Mock
    private TaskMapper taskMapper;
    
    @InjectMocks
    private TaskRepositoryAdapter taskRepositoryAdapter;
    
    private Task task;
    private TaskEntity taskEntity;
    private TaskId taskId;
    private UserId userId;

    @BeforeEach
    void setUp() {
        taskId = new TaskId(UUID.randomUUID());
        userId = new UserId(UUID.randomUUID());
        task = Task.createTask(
            userId,
            new Title("Tarefa Teste"),
            new Description("Descrição da tarefa")
        );
        
        taskEntity = new TaskEntity();
        taskEntity.setId(taskId.value());
        taskEntity.setUserId(userId.value());
        taskEntity.setTitle("Tarefa Teste");
        taskEntity.setDescription("Descrição da tarefa");
    }

    @Test
    void deveSalvarTask() {
        when(taskMapper.toEntity(task)).thenReturn(taskEntity);
        when(taskJpaRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDomain(taskEntity)).thenReturn(task);

        Task resultado = taskRepositoryAdapter.save(task);

        assertNotNull(resultado);
        assertEquals(task, resultado);
        verify(taskMapper).toEntity(task);
        verify(taskJpaRepository).save(taskEntity);
        verify(taskMapper).toDomain(taskEntity);
    }

    @Test
    void deveBuscarTaskPorId() {
        when(taskJpaRepository.findById(taskId.value())).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDomain(taskEntity)).thenReturn(task);

        Optional<Task> resultado = taskRepositoryAdapter.findById(taskId);

        assertTrue(resultado.isPresent());
        assertEquals(task, resultado.get());
        verify(taskJpaRepository).findById(taskId.value());
        verify(taskMapper).toDomain(taskEntity);
    }

    @Test
    void deveRetornarVazioQuandoTaskNaoExistePorId() {
        when(taskJpaRepository.findById(taskId.value())).thenReturn(Optional.empty());

        Optional<Task> resultado = taskRepositoryAdapter.findById(taskId);

        assertFalse(resultado.isPresent());
        verify(taskJpaRepository).findById(taskId.value());
        verify(taskMapper, never()).toDomain(any());
    }

    @Test
    void deveBuscarTasksPorUserId() {
        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setId(UUID.randomUUID());
        taskEntity2.setUserId(userId.value());
        
        Task task2 = Task.createTask(
            userId,
            new Title("Segunda Tarefa"),
            new Description("Segunda descrição")
        );
        
        List<TaskEntity> entities = List.of(taskEntity, taskEntity2);
        
        when(taskJpaRepository.findByUserId(userId.value())).thenReturn(entities);
        when(taskMapper.toDomain(taskEntity)).thenReturn(task);
        when(taskMapper.toDomain(taskEntity2)).thenReturn(task2);

        List<Task> resultado = taskRepositoryAdapter.findByUserId(userId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(task, resultado.get(0));
        assertEquals(task2, resultado.get(1));
        verify(taskJpaRepository).findByUserId(userId.value());
        verify(taskMapper, times(2)).toDomain(any(TaskEntity.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaTasksParaUsuario() {
        when(taskJpaRepository.findByUserId(userId.value())).thenReturn(List.of());

        List<Task> resultado = taskRepositoryAdapter.findByUserId(userId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(taskJpaRepository).findByUserId(userId.value());
        verify(taskMapper, never()).toDomain(any());
    }

    @Test
    void deveDeletarTaskPorId() {
        taskRepositoryAdapter.deleteById(taskId);

        verify(taskJpaRepository).deleteById(taskId.value());
    }

    @Test
    void deveVerificarSeTaskExistePorId() {
        when(taskJpaRepository.existsById(taskId.value())).thenReturn(true);

        boolean resultado = taskRepositoryAdapter.existsById(taskId);

        assertTrue(resultado);
        verify(taskJpaRepository).existsById(taskId.value());
    }

    @Test
    void deveRetornarFalsoQuandoTaskNaoExistePorId() {
        when(taskJpaRepository.existsById(taskId.value())).thenReturn(false);

        boolean resultado = taskRepositoryAdapter.existsById(taskId);

        assertFalse(resultado);
        verify(taskJpaRepository).existsById(taskId.value());
    }
}