package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private DeleteTaskUseCase deleteTaskUseCase;

    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
    }

    @Test
    void shouldDeleteTaskAndSubTasksSuccessfully() {
        when(taskRepository.existsById(any(TaskId.class))).thenReturn(true);

        deleteTaskUseCase.execute(taskId);

        verify(taskRepository).existsById(any(TaskId.class));
        verify(subTaskRepository).deleteByTaskId(any(TaskId.class));
        verify(taskRepository).deleteById(any(TaskId.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.existsById(any(TaskId.class))).thenReturn(false);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> deleteTaskUseCase.execute(taskId)
        );

        assertEquals("Tarefa não encontrada", exception.getMessage());
        verify(taskRepository).existsById(any(TaskId.class));
        verify(subTaskRepository, never()).deleteByTaskId(any(TaskId.class));
        verify(taskRepository, never()).deleteById(any(TaskId.class));
    }

    @Test
    void shouldDeleteSubTasksBeforeTask() {
        when(taskRepository.existsById(any(TaskId.class))).thenReturn(true);

        deleteTaskUseCase.execute(taskId);

        var inOrder = inOrder(subTaskRepository, taskRepository);
        inOrder.verify(taskRepository).existsById(any(TaskId.class));
        inOrder.verify(subTaskRepository).deleteByTaskId(any(TaskId.class));
        inOrder.verify(taskRepository).deleteById(any(TaskId.class));
    }

    @Test
    void shouldCreateCorrectTaskIdValueObject() {
        when(taskRepository.existsById(any(TaskId.class))).thenReturn(true);

        deleteTaskUseCase.execute(taskId);

        verify(taskRepository).existsById(argThat(taskIdVO -> 
            taskIdVO instanceof TaskId && taskIdVO.value().equals(taskId)
        ));
        verify(subTaskRepository).deleteByTaskId(argThat(taskIdVO -> 
            taskIdVO instanceof TaskId && taskIdVO.value().equals(taskId)
        ));
        verify(taskRepository).deleteById(argThat(taskIdVO -> 
            taskIdVO instanceof TaskId && taskIdVO.value().equals(taskId)
        ));
    }

    @Test
    void shouldUseTransactionalAnnotation() {
        when(taskRepository.existsById(any(TaskId.class))).thenReturn(true);

        deleteTaskUseCase.execute(taskId);

        verify(taskRepository).existsById(any(TaskId.class));
        verify(subTaskRepository).deleteByTaskId(any(TaskId.class));
        verify(taskRepository).deleteById(any(TaskId.class));
    }
}