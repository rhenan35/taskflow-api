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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTaskStatusUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UpdateTaskStatusUseCase useCase;

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
    void deveAtualizarStatusDaTarefa() {
        Task updatedTask = Task.fromExisting(
            new TaskId(taskId),
            task.getUserId(),
            task.getTitle(),
            task.getDescription(),
            ActivityStatus.IN_PROGRESS,
            task.getCreatedAt(),
            null
        );

        when(taskRepository.findById(new TaskId(taskId))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponse result = useCase.execute(taskId, ActivityStatus.IN_PROGRESS);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(taskId);
        assertThat(result.status()).isEqualTo(ActivityStatus.IN_PROGRESS);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        when(taskRepository.findById(new TaskId(taskId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(taskId, ActivityStatus.IN_PROGRESS))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Tarefa não encontrada");
    }

    @Test
    void deveCompletarTarefa() {
        Task completedTask = Task.fromExisting(
            new TaskId(taskId),
            task.getUserId(),
            task.getTitle(),
            task.getDescription(),
            ActivityStatus.COMPLETED,
            task.getCreatedAt(),
            Instant.now()
        );

        when(taskRepository.findById(new TaskId(taskId))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(completedTask);

        TaskResponse result = useCase.execute(taskId, ActivityStatus.COMPLETED);

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(ActivityStatus.COMPLETED);
        verify(taskRepository).save(any(Task.class));
    }
}