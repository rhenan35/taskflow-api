package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.Task;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTaskByIdUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private FindTaskByIdUseCase useCase;

    private UUID taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = Task.fromExisting(
            new TaskId(taskId),
            new UserId(UUID.randomUUID()),
            "Test Task",
            "Test Description",
            ActivityStatus.PENDING,
            Instant.now(),
            null
        );
    }

    @Test
    void deveRetornarTarefaQuandoEncontrada() {
        when(taskRepository.findById(new TaskId(taskId))).thenReturn(Optional.of(task));

        TaskResponse result = useCase.execute(taskId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(taskId);
        assertThat(result.title()).isEqualTo("Test Task");
        assertThat(result.description()).isEqualTo("Test Description");
        assertThat(result.status()).isEqualTo(ActivityStatus.PENDING);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        when(taskRepository.findById(new TaskId(taskId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(taskId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Tarefa não encontrada");
    }
}