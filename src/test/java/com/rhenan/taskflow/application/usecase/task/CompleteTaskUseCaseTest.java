package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CompleteTaskUseCase completeTaskUseCase;

    private Task pendingTask;
    private Task inProgressTask;
    private Task completedTask;
    private TaskId taskId;

    @BeforeEach
    void setUp() {
        taskId = TaskId.fromString("550e8400-e29b-41d4-a716-446655440000");
        
        pendingTask = Task.createTask(
            UserId.fromString("550e8400-e29b-41d4-a716-446655440001"),
            new Title("Pending Task"),
            new Description("Pending Task Description")
        );
        
        inProgressTask = Task.createTask(
            UserId.fromString("550e8400-e29b-41d4-a716-446655440001"),
            new Title("In Progress Task"),
            new Description("In Progress Task Description")
        );
        inProgressTask.updateStatus(ActivityStatus.IN_PROGRESS);
        
        completedTask = Task.createTask(
            UserId.fromString("550e8400-e29b-41d4-a716-446655440001"),
            new Title("Completed Task"),
            new Description("Completed Task Description")
        );
        completedTask.finish();
    }

    @Test
    void shouldCompletePendingTask() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(pendingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(pendingTask);

        TaskResponse response = completeTaskUseCase.execute(taskId.value());

        assertNotNull(response);
        assertEquals(ActivityStatus.COMPLETED, response.status());
        assertEquals(ActivityStatus.COMPLETED, pendingTask.getStatus());
        assertNotNull(pendingTask.getCompletedAt());
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(pendingTask);
    }

    @Test
    void shouldCompleteInProgressTask() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(inProgressTask));
        when(taskRepository.save(any(Task.class))).thenReturn(inProgressTask);

        TaskResponse response = completeTaskUseCase.execute(taskId.value());

        assertNotNull(response);
        assertEquals(ActivityStatus.COMPLETED, response.status());
        assertEquals(ActivityStatus.COMPLETED, inProgressTask.getStatus());
        assertNotNull(inProgressTask.getCompletedAt());
        
        verify(taskRepository).save(inProgressTask);
    }

    @Test
    void shouldThrowExceptionWhenTaskAlreadyCompleted() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(completedTask));

        assertThrows(BusinessRuleException.class, () -> 
            completeTaskUseCase.execute(taskId.value())
        );
        
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            completeTaskUseCase.execute(taskId.value())
        );
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldReturnCorrectTaskResponse() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(pendingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(pendingTask);

        TaskResponse response = completeTaskUseCase.execute(taskId.value());

        assertNotNull(response);
        assertEquals(pendingTask.getId().value(), response.id());
        assertEquals(pendingTask.getTitle().value(), response.title());
        assertEquals(pendingTask.getDescription().value(), response.description());
        assertEquals(pendingTask.getUserId().value(), response.userId());
        assertEquals(ActivityStatus.COMPLETED, response.status());
        assertNotNull(response.completedAt());
    }
}