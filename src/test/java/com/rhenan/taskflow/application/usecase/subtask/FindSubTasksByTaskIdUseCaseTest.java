package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.repository.TaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindSubTasksByTaskIdUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private FindSubTasksByTaskIdUseCase findSubTasksByTaskIdUseCase;

    private UUID taskId;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        
        subTask1 = SubTask.fromExisting(
            new SubTaskId(UUID.randomUUID()),
            new TaskId(taskId),
            "SubTask 1",
            "Description 1",
            ActivityStatus.PENDING,
            Instant.now(),
            null
        );
        
        subTask2 = SubTask.fromExisting(
            new SubTaskId(UUID.randomUUID()),
            new TaskId(taskId),
            "SubTask 2",
            "Description 2",
            ActivityStatus.COMPLETED,
            Instant.now(),
            Instant.now()
        );
    }

    @Test
    void execute_ShouldReturnSubTasksList_WhenTaskExistsAndHasSubTasks() {
        when(taskRepository.existsById(any(TaskId.class)))
            .thenReturn(true);
        when(subTaskRepository.findByTaskId(any(TaskId.class)))
            .thenReturn(List.of(subTask1, subTask2));

        List<SubTaskResponse> responses = findSubTasksByTaskIdUseCase.execute(taskId);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).title()).isEqualTo("SubTask 1");
        assertThat(responses.get(0).status()).isEqualTo(ActivityStatus.PENDING);
        assertThat(responses.get(1).title()).isEqualTo("SubTask 2");
        assertThat(responses.get(1).status()).isEqualTo(ActivityStatus.COMPLETED);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenTaskExistsButHasNoSubTasks() {
        when(taskRepository.existsById(any(TaskId.class)))
            .thenReturn(true);
        when(subTaskRepository.findByTaskId(any(TaskId.class)))
            .thenReturn(List.of());

        List<SubTaskResponse> responses = findSubTasksByTaskIdUseCase.execute(taskId);

        assertThat(responses).isEmpty();
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenTaskDoesNotExist() {
        when(taskRepository.existsById(any(TaskId.class)))
            .thenReturn(false);

        assertThatThrownBy(() -> findSubTasksByTaskIdUseCase.execute(taskId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Tarefa não encontrada");
    }
}