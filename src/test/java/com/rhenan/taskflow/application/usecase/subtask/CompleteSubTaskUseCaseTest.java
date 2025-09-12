package com.rhenan.taskflow.application.usecase.subtask;

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
class CompleteSubTaskUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private CompleteSubTaskUseCase completeSubTaskUseCase;

    private UUID subTaskUuid;
    private SubTaskId subTaskId;
    private TaskId taskId;
    private SubTask subTask;

    @BeforeEach
    void setUp() {
        subTaskUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        subTaskId = new SubTaskId(subTaskUuid);
        taskId = TaskId.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        subTask = SubTask.newSubTask(taskId, "Test SubTask", "Test Description");
    }

    @Test
    void shouldCompleteSubTaskSuccessfully() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));
        when(subTaskRepository.save(any(SubTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubTaskResponse response = completeSubTaskUseCase.execute(subTaskUuid);

        assertNotNull(response);
        assertEquals("Test SubTask", response.title());
        assertEquals("Test Description", response.description());
        assertEquals(taskId.value(), response.taskId());
        assertNotNull(response.status());
        assertNotNull(response.completedAt());
        
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(subTask);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSubTaskDoesNotExist() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> completeSubTaskUseCase.execute(subTaskUuid)
        );

        assertEquals("Subtarefa não encontrada", exception.getMessage());
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void shouldCallFinishMethodOnSubTask() {
        SubTask spySubTask = spy(subTask);
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(spySubTask));
        when(subTaskRepository.save(any(SubTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        completeSubTaskUseCase.execute(subTaskUuid);

        verify(spySubTask).finish();
        verify(subTaskRepository).save(spySubTask);
    }
}