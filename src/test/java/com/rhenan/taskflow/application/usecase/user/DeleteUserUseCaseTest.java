package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.domain.exception.NotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    private UUID userUuid;
    private UserId userId;

    @BeforeEach
    void setUp() {
        userUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        userId = new UserId(userUuid);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> deleteUserUseCase.execute(userUuid));

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> deleteUserUseCase.execute(userUuid)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void shouldCallExistsBeforeDelete() {
        when(userRepository.existsById(userId)).thenReturn(true);

        deleteUserUseCase.execute(userUuid);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }
}