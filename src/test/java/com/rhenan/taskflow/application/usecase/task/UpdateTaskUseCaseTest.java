package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.request.UpdateTaskRequest;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;
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
class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UpdateTaskUseCase updateTaskUseCase;

    private Task existingTask;
    private TaskId taskId;
    private UpdateTaskRequest updateRequest;

    @BeforeEach
    void setUp() {
        taskId = TaskId.fromString("550e8400-e29b-41d4-a716-446655440000");
        existingTask = Task.createTask(
            UserId.fromString("550e8400-e29b-41d4-a716-446655440001"),
            "Original Title",
            "Original Description"
        );
        updateRequest = new UpdateTaskRequest("Updated Title", "Updated Description");
    }

    @Test
    void shouldUpdateTaskTitleAndDescription() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponse response = updateTaskUseCase.execute(taskId.value(), updateRequest);

        assertNotNull(response);
        assertEquals("Updated Title", response.title());
        assertEquals("Updated Description", response.description());
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldUpdateOnlyTitle() {
        UpdateTaskRequest titleOnlyRequest = new UpdateTaskRequest("New Title", null);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponse response = updateTaskUseCase.execute(taskId.value(), titleOnlyRequest);

        assertNotNull(response);
        assertEquals("New Title", response.title());
        assertEquals("Original Description", response.description());
        
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldUpdateOnlyDescription() {
        UpdateTaskRequest descriptionOnlyRequest = new UpdateTaskRequest(null, "New Description");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponse response = updateTaskUseCase.execute(taskId.value(), descriptionOnlyRequest);

        assertNotNull(response);
        assertEquals("Original Title", response.title());
        assertEquals("New Description", response.description());
        
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            updateTaskUseCase.execute(taskId.value(), updateRequest)
        );
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldThrowBusinessRuleExceptionWhenUpdatingCompletedTask() {
        existingTask.finish();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        assertThrows(BusinessRuleException.class, () -> 
            updateTaskUseCase.execute(taskId.value(), updateRequest)
        );
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldNotUpdateWhenBothFieldsAreNull() {
        UpdateTaskRequest emptyRequest = new UpdateTaskRequest(null, null);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponse response = updateTaskUseCase.execute(taskId.value(), emptyRequest);

        assertNotNull(response);
        assertEquals("Original Title", response.title());
        assertEquals("Original Description", response.description());
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }
}