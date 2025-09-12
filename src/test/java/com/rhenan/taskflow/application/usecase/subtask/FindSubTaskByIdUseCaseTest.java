package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.NotFoundException;
import com.rhenan.taskflow.domain.model.SubTask;
import com.rhenan.taskflow.domain.repository.SubTaskRepository;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindSubTaskByIdUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private FindSubTaskByIdUseCase findSubTaskByIdUseCase;

    private SubTask subTask;
    private UUID subTaskId;

    @BeforeEach
    void setUp() {
        subTaskId = UUID.randomUUID();
        subTask = SubTask.fromExisting(
            new SubTaskId(subTaskId),
            new TaskId(UUID.randomUUID()),
            "Test SubTask",
            "Test Description",
            ActivityStatus.PENDING,
            Instant.now(),
            null
        );
    }

    @Test
    void execute_ShouldReturnSubTaskResponse_WhenSubTaskExists() {
        when(subTaskRepository.findById(any(SubTaskId.class)))
            .thenReturn(Optional.of(subTask));

        SubTaskResponse response = findSubTaskByIdUseCase.execute(subTaskId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(subTaskId);
        assertThat(response.title()).isEqualTo("Test SubTask");
        assertThat(response.description()).isEqualTo("Test Description");
        assertThat(response.status()).isEqualTo(ActivityStatus.PENDING);
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenSubTaskDoesNotExist() {
        when(subTaskRepository.findById(any(SubTaskId.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> findSubTaskByIdUseCase.execute(subTaskId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Subtarefa não encontrada");
    }
}