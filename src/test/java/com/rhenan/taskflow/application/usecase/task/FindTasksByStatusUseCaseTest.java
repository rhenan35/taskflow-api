package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTasksByStatusUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private FindTasksByStatusUseCase useCase;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = Task.fromExisting(
            new TaskId(UUID.randomUUID()),
            new UserId(UUID.randomUUID()),
            "Task 1",
            "Description 1",
            ActivityStatus.PENDING,
            Instant.now(),
            null
        );
        
        task2 = Task.fromExisting(
            new TaskId(UUID.randomUUID()),
            new UserId(UUID.randomUUID()),
            "Task 2",
            "Description 2",
            ActivityStatus.PENDING,
            Instant.now(),
            null
        );
    }

    @Test
    void deveRetornarTarefasComStatusEspecifico() {
        List<Task> tasks = List.of(task1, task2);
        when(taskRepository.findByStatus(ActivityStatus.PENDING)).thenReturn(tasks);

        List<TaskResponse> result = useCase.execute(ActivityStatus.PENDING);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("Task 1");
        assertThat(result.get(1).title()).isEqualTo("Task 2");
        assertThat(result.get(0).status()).isEqualTo(ActivityStatus.PENDING);
        assertThat(result.get(1).status()).isEqualTo(ActivityStatus.PENDING);
        verify(taskRepository).findByStatus(ActivityStatus.PENDING);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverTarefasComStatus() {
        when(taskRepository.findByStatus(ActivityStatus.COMPLETED)).thenReturn(List.of());

        List<TaskResponse> result = useCase.execute(ActivityStatus.COMPLETED);

        assertThat(result).isEmpty();
        verify(taskRepository).findByStatus(ActivityStatus.COMPLETED);
    }

    @Test
    void deveRetornarTarefasEmProgresso() {
        List<Task> inProgressTasks = List.of(task1);
        when(taskRepository.findByStatus(ActivityStatus.IN_PROGRESS)).thenReturn(inProgressTasks);

        List<TaskResponse> result = useCase.execute(ActivityStatus.IN_PROGRESS);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(ActivityStatus.PENDING);
        verify(taskRepository).findByStatus(ActivityStatus.IN_PROGRESS);
    }
}