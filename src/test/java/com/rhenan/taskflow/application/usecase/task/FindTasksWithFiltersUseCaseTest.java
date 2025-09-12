package com.rhenan.taskflow.application.usecase.task;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTasksWithFiltersUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private FindTasksWithFiltersUseCase useCase;

    private Task task;
    private TaskFilterRequest filters;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        task = Task.fromExisting(
            new TaskId(UUID.randomUUID()),
            new UserId(UUID.randomUUID()),
            "Test Task",
            "Test Description",
            ActivityStatus.PENDING,
            Instant.now(),
            null
        );

        filters = TaskFilterRequest.builder()
            .status(ActivityStatus.PENDING)
            .title("Test")
            .build();

        pageRequest = PageRequest.of(0, 10, "createdAt", "desc");
    }

    @Test
    void deveRetornarTarefasComFiltros() {
        PageResponse<Task> taskPage = PageResponse.of(
            List.of(task),
            0,
            10,
            1L
        );

        when(taskRepository.findWithFilters(filters, pageRequest)).thenReturn(taskPage);

        PageResponse<TaskResponse> result = useCase.execute(filters, pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).title()).isEqualTo("Test Task");
        assertThat(result.content().get(0).status()).isEqualTo(ActivityStatus.PENDING);
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrarTarefas() {
        PageResponse<Task> emptyPage = PageResponse.of(
            List.of(),
            0,
            10,
            0L
        );

        when(taskRepository.findWithFilters(filters, pageRequest)).thenReturn(emptyPage);

        PageResponse<TaskResponse> result = useCase.execute(filters, pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0L);
    }
}