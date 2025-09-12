package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.Email;

import com.rhenan.taskflow.domain.valueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserUseCase findUserUseCase;

    private UUID userUuid;
    private UserId userId;
    private User user;

    @BeforeEach
    void setUp() {
        userUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        userId = new UserId(userUuid);
        
        user = User.newUser(
            "Test User",
            new Email("test@example.com")
        );
    }

    @Test
    void shouldFindUserSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = findUserUseCase.execute(userUuid);

        assertNotNull(response);
        assertEquals("Test User", response.name());
        assertEquals("test@example.com", response.email());
        assertNotNull(response.id());
        
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> findUserUseCase.execute(userUuid)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldUseReadOnlyTransaction() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        findUserUseCase.execute(userUuid);

        verify(userRepository).findById(userId);
    }
}