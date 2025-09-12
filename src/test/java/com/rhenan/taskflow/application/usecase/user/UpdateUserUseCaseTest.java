package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.request.UpdateUserRequest;
import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.Name;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private UUID userUuid;
    private UserId userId;
    private User existingUser;
    private UpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        userUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        userId = new UserId(userUuid);
        
        existingUser = User.newUser(
            new Name("Original Name"),
            new Email("original@example.com")
        );
        
        updateRequest = new UpdateUserRequest(
            "Updated Name",
            "updated@example.com"
        );
    }

    @Test
    void shouldUpdateUserWithNameAndEmail() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = updateUserUseCase.execute(userUuid, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Name", response.name());
        assertEquals("updated@example.com", response.email());
        assertNotNull(response.id());
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserWithOnlyName() {
        UpdateUserRequest requestWithOnlyName = new UpdateUserRequest("Updated Name", null);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = updateUserUseCase.execute(userUuid, requestWithOnlyName);

        assertNotNull(response);
        assertEquals("Updated Name", response.name());
        assertEquals("original@example.com", response.email());
        assertNotNull(response.id());
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserWithOnlyEmail() {
        UpdateUserRequest requestWithOnlyEmail = new UpdateUserRequest(null, "updated@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = updateUserUseCase.execute(userUuid, requestWithOnlyEmail);

        assertNotNull(response);
        assertEquals("Original Name", response.name());
        assertEquals("updated@example.com", response.email());
        assertNotNull(response.id());
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> updateUserUseCase.execute(userUuid, updateRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}