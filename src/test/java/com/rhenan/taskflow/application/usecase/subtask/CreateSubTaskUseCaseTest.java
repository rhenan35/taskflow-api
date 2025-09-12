package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
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
class CreateSubTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CreateSubTaskUseCase createSubTaskUseCase;

    private Task existingTask;
    private TaskId taskId;
    private CreateSubTaskRequest createRequest;

    @BeforeEach
    void setUp() {
        taskId = TaskId.fromString("550e8400-e29b-41d4-a716-446655440000");
        UserId userId = UserId.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        existingTask = Task.createTask(
            userId,
            new Title("Parent Task"),
            new Description("Parent Task Description")
        );
        
        createRequest = new CreateSubTaskRequest(
            taskId.value(),
            "SubTask Title",
            "SubTask Description"
        );
    }

    @Test
    void shouldCreateSubTaskWithTitleAndDescription() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        SubTaskResponse response = createSubTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals("SubTask Title", response.title());
        assertEquals("SubTask Description", response.description());
        assertNotNull(response.id());
        assertEquals(existingTask.getId().value(), response.taskId());
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
        assertEquals(1, existingTask.getSubTask().size());
    }

    @Test
    void shouldCreateSubTaskWithOnlyTitle() {
        CreateSubTaskRequest titleOnlyRequest = new CreateSubTaskRequest(
            taskId.value(),
            "SubTask Title Only",
            null
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        SubTaskResponse response = createSubTaskUseCase.execute(titleOnlyRequest);

        assertNotNull(response);
        assertEquals("SubTask Title Only", response.title());
        assertNull(response.description());
        assertNotNull(response.id());
        assertEquals(existingTask.getId().value(), response.taskId());
        
        verify(taskRepository).save(existingTask);
        assertEquals(1, existingTask.getSubTask().size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            createSubTaskUseCase.execute(createRequest)
        );
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldAddSubTaskToExistingTask() {
        existingTask.addSubTask(new Title("Existing SubTask"), null);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        SubTaskResponse response = createSubTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals("SubTask Title", response.title());
        
        verify(taskRepository).save(existingTask);
        assertEquals(2, existingTask.getSubTask().size());
    }

    @Test
    void shouldCreateSubTaskWithValidTaskId() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        SubTaskResponse response = createSubTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals(existingTask.getId().value(), response.taskId());
        assertNotNull(response.id());
        verify(taskRepository).findById(taskId);
    }
}