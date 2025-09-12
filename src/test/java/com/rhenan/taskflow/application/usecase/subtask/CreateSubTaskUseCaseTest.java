package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSubTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private CreateSubTaskUseCase createSubTaskUseCase;

    private TaskId taskId;
    private CreateSubTaskRequest createRequest;
    private SubTask mockSubTask;

    @BeforeEach
    void setUp() {
        taskId = TaskId.fromString("550e8400-e29b-41d4-a716-446655440000");
        
        createRequest = new CreateSubTaskRequest(
            taskId.value(),
            "SubTask Title",
            "SubTask Description"
        );
        
        mockSubTask = SubTask.newSubTask(taskId, "SubTask Title", "SubTask Description");
    }

    @Test
    void shouldCreateSubTaskWithTitleAndDescription() {
        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(mockSubTask);

        SubTaskResponse response = createSubTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals("SubTask Title", response.title());
        assertEquals("SubTask Description", response.description());
        assertNotNull(response.id());
        assertEquals(taskId.value(), response.taskId());
        
        verify(taskRepository).existsById(taskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldCreateSubTaskWithOnlyTitle() {
        CreateSubTaskRequest titleOnlyRequest = new CreateSubTaskRequest(
            taskId.value(),
            "SubTask Title Only",
            null
        );
        SubTask titleOnlySubTask = SubTask.newSubTask(taskId, "SubTask Title Only", null);
        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(titleOnlySubTask);

        SubTaskResponse response = createSubTaskUseCase.execute(titleOnlyRequest);

        assertNotNull(response);
        assertEquals("SubTask Title Only", response.title());
        assertEquals("", response.description());
        
        verify(taskRepository).existsById(taskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> 
            createSubTaskUseCase.execute(createRequest)
        );
        
        verify(taskRepository).existsById(taskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void shouldAddSubTaskToExistingTask() {
        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(mockSubTask);

        SubTaskResponse response = createSubTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals("SubTask Title", response.title());
        
        verify(taskRepository).existsById(taskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldCreateSubTaskWithValidTaskId() {
        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(mockSubTask);

        SubTaskResponse response = createSubTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals(taskId.value(), response.taskId());
        assertNotNull(response.id());
        verify(taskRepository).existsById(taskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }
}