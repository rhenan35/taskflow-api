package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.request.CreateTaskRequest;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.UserId;
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
class CreateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;

    private UUID userUuid;
    private UserId userId;
    private CreateTaskRequest createRequest;

    @BeforeEach
    void setUp() {
        userUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        userId = new UserId(userUuid);
        
        createRequest = new CreateTaskRequest(
            userUuid,
            "Test Task",
            "Test Description"
        );
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = createTaskUseCase.execute(createRequest);

        assertNotNull(response);
        assertEquals("Test Task", response.title());
        assertEquals("Test Description", response.description());
        assertEquals(userUuid, response.userId());
        assertNotNull(response.id());
        assertNotNull(response.status());
        assertNotNull(response.createdAt());
        
        verify(userRepository).existsById(userId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldCreateTaskWithNullDescription() {
        CreateTaskRequest requestWithNullDescription = new CreateTaskRequest(
            userUuid,
            "Test Task",
            null
        );
        
        when(userRepository.existsById(userId)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = createTaskUseCase.execute(requestWithNullDescription);

        assertNotNull(response);
        assertEquals("Test Task", response.title());
        assertNull(response.description());
        assertEquals(userUuid, response.userId());
        
        verify(userRepository).existsById(userId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> createTaskUseCase.execute(createRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository).existsById(userId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldCheckUserExistenceBeforeCreatingTask() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        createTaskUseCase.execute(createRequest);

        verify(userRepository).existsById(userId);
        verify(taskRepository).save(any(Task.class));
    }
}