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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.fromExisting(
            new UserId(userId),
            "Test User",
            new Email("test@example.com")
        );
    }

    @Test
    void execute_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(any(UserId.class)))
            .thenReturn(Optional.of(user));

        UserResponse response = findUserByIdUseCase.execute(userId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(userId);
        assertThat(response.name()).isEqualTo("Test User");
        assertThat(response.email()).isEqualTo("test@example.com");
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(any(UserId.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> findUserByIdUseCase.execute(userId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Usuário não encontrado");
    }
}