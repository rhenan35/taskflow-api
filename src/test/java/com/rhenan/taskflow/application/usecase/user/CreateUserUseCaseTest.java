package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.request.CreateUserRequest;
import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.Email;
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
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private CreateUserRequest createRequest;
    private User createdUser;

    @BeforeEach
    void setUp() {
        createRequest = new CreateUserRequest(
            "John Doe",
            "john.doe@example.com"
        );
        
        createdUser = User.newUser(
            "John Doe",
            new Email("john.doe@example.com")
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        
        UserResponse response = createUserUseCase.execute(createRequest);
        
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldSaveUserToRepository() {
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        
        createUserUseCase.execute(createRequest);
        
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnCorrectUserResponse() {
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        
        UserResponse response = createUserUseCase.execute(createRequest);
        
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());
        assertNotNull(response.id());
    }
}