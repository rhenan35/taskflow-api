package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSubTaskUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private DeleteSubTaskUseCase deleteSubTaskUseCase;

    private UUID subTaskUuid;
    private SubTaskId subTaskId;

    @BeforeEach
    void setUp() {
        subTaskUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        subTaskId = new SubTaskId(subTaskUuid);
    }

    @Test
    void shouldDeleteSubTaskSuccessfully() {
        when(subTaskRepository.existsById(subTaskId)).thenReturn(true);

        assertDoesNotThrow(() -> deleteSubTaskUseCase.execute(subTaskUuid));

        verify(subTaskRepository).existsById(subTaskId);
        verify(subTaskRepository).deleteById(subTaskId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSubTaskDoesNotExist() {
        when(subTaskRepository.existsById(subTaskId)).thenReturn(false);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> deleteSubTaskUseCase.execute(subTaskUuid)
        );

        assertEquals("Subtarefa não encontrada", exception.getMessage());
        verify(subTaskRepository).existsById(subTaskId);
        verify(subTaskRepository, never()).deleteById(subTaskId);
    }

    @Test
    void shouldCallExistsBeforeDelete() {
        when(subTaskRepository.existsById(subTaskId)).thenReturn(true);

        deleteSubTaskUseCase.execute(subTaskUuid);

        verify(subTaskRepository).existsById(subTaskId);
        verify(subTaskRepository).deleteById(subTaskId);
    }
}