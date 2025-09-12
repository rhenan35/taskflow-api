package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.request.UpdateSubTaskRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateSubTaskUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private UpdateSubTaskUseCase updateSubTaskUseCase;

    private UUID subTaskUuid;
    private SubTaskId subTaskId;
    private TaskId taskId;
    private SubTask existingSubTask;
    private UpdateSubTaskRequest updateRequest;

    @BeforeEach
    void setUp() {
        subTaskUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        subTaskId = new SubTaskId(subTaskUuid);
        taskId = TaskId.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        existingSubTask = SubTask.newSubTask(
             taskId,
             "Original Title",
             "Original Description"
         );
        
        updateRequest = new UpdateSubTaskRequest(
            "Updated Title",
            "Updated Description"
        );
    }

    @Test
    void shouldUpdateSubTaskWithTitleAndDescription() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(existingSubTask));
        when(subTaskRepository.save(any(SubTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubTaskResponse response = updateSubTaskUseCase.execute(subTaskUuid, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Title", response.title());
        assertEquals("Updated Description", response.description());
        assertEquals(taskId.value(), response.taskId());
        
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldUpdateSubTaskWithOnlyTitle() {
        UpdateSubTaskRequest requestWithOnlyTitle = new UpdateSubTaskRequest("Updated Title", null);
        
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(existingSubTask));
        when(subTaskRepository.save(any(SubTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubTaskResponse response = updateSubTaskUseCase.execute(subTaskUuid, requestWithOnlyTitle);

        assertNotNull(response);
        assertEquals("Updated Title", response.title());
        assertEquals("Original Description", response.description());
        assertEquals(taskId.value(), response.taskId());
        
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldUpdateSubTaskWithOnlyDescription() {
        UpdateSubTaskRequest requestWithOnlyDescription = new UpdateSubTaskRequest(null, "Updated Description");
        
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(existingSubTask));
        when(subTaskRepository.save(any(SubTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubTaskResponse response = updateSubTaskUseCase.execute(subTaskUuid, requestWithOnlyDescription);

        assertNotNull(response);
        assertEquals("Original Title", response.title());
        assertEquals("Updated Description", response.description());
        assertEquals(taskId.value(), response.taskId());
        
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSubTaskDoesNotExist() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> updateSubTaskUseCase.execute(subTaskUuid, updateRequest)
        );

        assertEquals("Subtarefa não encontrada", exception.getMessage());
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void shouldKeepOriginalTitleWhenNewTitleIsNull() {
        UpdateSubTaskRequest requestWithNullTitle = new UpdateSubTaskRequest(null, "New Description");
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(existingSubTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(existingSubTask);

        SubTaskResponse response = updateSubTaskUseCase.execute(subTaskUuid, requestWithNullTitle);

        assertNotNull(response);
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldKeepOriginalDescriptionWhenNewDescriptionIsNull() {
        UpdateSubTaskRequest requestWithNullDescription = new UpdateSubTaskRequest("New Title", null);
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(existingSubTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(existingSubTask);

        SubTaskResponse response = updateSubTaskUseCase.execute(subTaskUuid, requestWithNullDescription);

        assertNotNull(response);
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(any(SubTask.class));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsEmpty() {
        UpdateSubTaskRequest requestWithEmptyTitle = new UpdateSubTaskRequest("", "Valid Description");
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(existingSubTask));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> updateSubTaskUseCase.execute(subTaskUuid, requestWithEmptyTitle)
        );

        assertEquals("Title não pode ser vazio!", exception.getMessage());
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }
}