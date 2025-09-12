package com.rhenan.taskflow.application.usecase.user;

import com.rhenan.taskflow.application.dto.request.CreateUserRequest;
import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.domain.factory.UserFactory;
import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.repository.UserRepository;
import com.rhenan.taskflow.domain.valueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
            new Name("John Doe"),
            new Email("john.doe@example.com")
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        try (MockedStatic<UserFactory> userFactoryMock = mockStatic(UserFactory.class)) {
            userFactoryMock.when(() -> UserFactory.createUser(
                anyString(), anyString()
            )).thenReturn(createdUser);
            
            when(userRepository.save(any(User.class))).thenReturn(createdUser);

            UserResponse response = createUserUseCase.execute(createRequest);

            assertNotNull(response);
            assertEquals("John Doe", response.name());
            assertEquals("john.doe@example.com", response.email());
            assertNotNull(response.id());
            
            userFactoryMock.verify(() -> UserFactory.createUser(
                "John Doe", "john.doe@example.com"
            ));
            verify(userRepository).save(createdUser);
        }
    }

    @Test
    void shouldCreateUserWithCorrectValueObjects() {
        try (MockedStatic<UserFactory> userFactoryMock = mockStatic(UserFactory.class)) {
            userFactoryMock.when(() -> UserFactory.createUser(
                anyString(), anyString()
            )).thenReturn(createdUser);
            
            when(userRepository.save(any(User.class))).thenReturn(createdUser);

            createUserUseCase.execute(createRequest);

            userFactoryMock.verify(() -> UserFactory.createUser(
                "John Doe", "john.doe@example.com"
            ));
        }
    }

    @Test
    void shouldSaveUserToRepository() {
        try (MockedStatic<UserFactory> userFactoryMock = mockStatic(UserFactory.class)) {
            userFactoryMock.when(() -> UserFactory.createUser(
                anyString(), anyString()
            )).thenReturn(createdUser);
            
            when(userRepository.save(any(User.class))).thenReturn(createdUser);

            createUserUseCase.execute(createRequest);

            verify(userRepository).save(createdUser);
        }
    }

    @Test
    void shouldReturnCorrectUserResponse() {
        try (MockedStatic<UserFactory> userFactoryMock = mockStatic(UserFactory.class)) {
            userFactoryMock.when(() -> UserFactory.createUser(
                anyString(), anyString()
            )).thenReturn(createdUser);
            
            when(userRepository.save(any(User.class))).thenReturn(createdUser);

            UserResponse response = createUserUseCase.execute(createRequest);

            assertNotNull(response);
            assertEquals(createdUser.getId().value(), response.id());
            assertEquals(createdUser.getName().value(), response.name());
            assertEquals(createdUser.getEmail().value(), response.email());
            assertNotNull(response.id());
        }
    }

    @Test
    void shouldCreateUserWithDifferentData() {
        CreateUserRequest differentRequest = new CreateUserRequest(
            "Jane Smith",
            "jane.smith@example.com"
        );
        
        User differentUser = User.newUser(
            new Name("Jane Smith"),
            new Email("jane.smith@example.com")
        );
        
        try (MockedStatic<UserFactory> userFactoryMock = mockStatic(UserFactory.class)) {
            userFactoryMock.when(() -> UserFactory.createUser(
                anyString(), anyString()
            )).thenReturn(differentUser);
            
            when(userRepository.save(any(User.class))).thenReturn(differentUser);

            UserResponse response = createUserUseCase.execute(differentRequest);

            assertNotNull(response);
            assertEquals("Jane Smith", response.name());
            assertEquals("jane.smith@example.com", response.email());
        }
    }
}