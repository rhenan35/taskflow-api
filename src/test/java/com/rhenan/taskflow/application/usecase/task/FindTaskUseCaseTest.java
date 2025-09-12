package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
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
class FindTaskUseCaseTest {

    @Mock(lenient = true)
    private TaskRepository taskRepository;

    @Mock(lenient = true)
    private SubTaskRepository subTaskRepository;



    @InjectMocks
    private FindTaskUseCase findTaskUseCase;

    private UUID taskId;
    private Task task;
    private List<SubTask> subTasks;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = mock(Task.class);
        subTasks = List.of();
    }

    @Test
    void shouldFindTaskWithSubTasksSuccessfully() {
        when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(task));
        when(subTaskRepository.findByTaskId(any(TaskId.class))).thenReturn(subTasks);
        
        when(task.getId()).thenReturn(new TaskId(taskId));
        when(task.getUserId()).thenReturn(mock(com.rhenan.taskflow.domain.valueObjects.UserId.class));
        when(task.getTitle()).thenReturn(mock(com.rhenan.taskflow.domain.valueObjects.Title.class));
        when(task.getStatus()).thenReturn(com.rhenan.taskflow.domain.enums.ActivityStatus.PENDING);
        when(task.getCreatedAt()).thenReturn(java.time.Instant.now());

        assertDoesNotThrow(() -> findTaskUseCase.execute(taskId));

        verify(taskRepository).findById(any(TaskId.class));
        verify(subTaskRepository).findByTaskId(any(TaskId.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> findTaskUseCase.execute(taskId)
        );

        assertEquals("Tarefa não encontrada", exception.getMessage());
        verify(taskRepository).findById(any(TaskId.class));
        verify(subTaskRepository, never()).findByTaskId(any(TaskId.class));
    }

    @Test
    void shouldUseReadOnlyTransaction() {
        when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(task));
        when(subTaskRepository.findByTaskId(any(TaskId.class))).thenReturn(subTasks);
        
        when(task.getId()).thenReturn(new TaskId(taskId));
        when(task.getUserId()).thenReturn(mock(com.rhenan.taskflow.domain.valueObjects.UserId.class));
        when(task.getTitle()).thenReturn(mock(com.rhenan.taskflow.domain.valueObjects.Title.class));
        when(task.getStatus()).thenReturn(com.rhenan.taskflow.domain.enums.ActivityStatus.PENDING);
        when(task.getCreatedAt()).thenReturn(java.time.Instant.now());

        assertDoesNotThrow(() -> findTaskUseCase.execute(taskId));

        verify(taskRepository).findById(any(TaskId.class));
        verify(subTaskRepository).findByTaskId(any(TaskId.class));
    }

    @Test
    void shouldCreateCorrectTaskIdValueObject() {
        when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(task));
        when(subTaskRepository.findByTaskId(any(TaskId.class))).thenReturn(subTasks);
        
        when(task.getId()).thenReturn(new TaskId(taskId));
        when(task.getUserId()).thenReturn(mock(com.rhenan.taskflow.domain.valueObjects.UserId.class));
        when(task.getTitle()).thenReturn(mock(com.rhenan.taskflow.domain.valueObjects.Title.class));
        when(task.getStatus()).thenReturn(com.rhenan.taskflow.domain.enums.ActivityStatus.PENDING);
        when(task.getCreatedAt()).thenReturn(java.time.Instant.now());

        assertDoesNotThrow(() -> findTaskUseCase.execute(taskId));

        verify(taskRepository).findById(argThat(taskIdVO -> 
            taskIdVO instanceof TaskId && taskIdVO.value().equals(taskId)
        ));
        verify(subTaskRepository).findByTaskId(argThat(taskIdVO -> 
            taskIdVO instanceof TaskId && taskIdVO.value().equals(taskId)
        ));
    }
}