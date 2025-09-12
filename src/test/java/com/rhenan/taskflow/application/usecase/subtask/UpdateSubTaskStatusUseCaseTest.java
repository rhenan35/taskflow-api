package com.rhenan.taskflow.application.usecase.subtask;

import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateSubTaskStatusUseCaseTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private UpdateSubTaskStatusUseCase updateSubTaskStatusUseCase;

    private UUID subTaskUuid;
    private SubTaskId subTaskId;
    private SubTask subTask;

    @BeforeEach
    void setUp() {
        subTaskUuid = UUID.randomUUID();
        subTaskId = new SubTaskId(subTaskUuid);
        subTask = SubTask.newSubTask(TaskId.newTask(), "Test SubTask", "Description");
    }

    @Test
    void deveAtualizarStatusComSucesso() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(subTask);

        SubTaskResponse response = updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.IN_PROGRESS);

        assertNotNull(response);
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(subTask);
    }

    @Test
    void deveCompletarSubTarefaEDefinirCompletedAt() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(subTask);

        SubTaskResponse response = updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.COMPLETED);

        assertNotNull(response);
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(subTask);
    }

    @Test
    void deveLancarExcecaoQuandoSubTarefaNaoExistir() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.COMPLETED)
        );

        assertEquals("Subtarefa não encontrada", exception.getMessage());
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void deveLancarExcecaoParaTransicaoInvalida() {
        subTask.updateStatus(ActivityStatus.IN_PROGRESS);
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));

        BusinessRuleException exception = assertThrows(
            BusinessRuleException.class,
            () -> updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.PENDING)
        );

        assertTrue(exception.getMessage().contains("Transição inválida"));
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void naoDevePermitirAlterarStatusDeSubTarefaConcluida() {
        subTask.updateStatus(ActivityStatus.COMPLETED);
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));

        BusinessRuleException exception = assertThrows(
            BusinessRuleException.class,
            () -> updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.IN_PROGRESS)
        );

        assertTrue(exception.getMessage().contains("Transição inválida"));
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void devePermitirTransicaoDePendingParaInProgress() {
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(subTask);

        SubTaskResponse response = updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.IN_PROGRESS);

        assertNotNull(response);
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(subTask);
    }

    @Test
    void devePermitirTransicaoDeInProgressParaCompleted() {
        subTask.updateStatus(ActivityStatus.IN_PROGRESS);
        when(subTaskRepository.findById(subTaskId)).thenReturn(Optional.of(subTask));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(subTask);

        SubTaskResponse response = updateSubTaskStatusUseCase.execute(subTaskUuid, ActivityStatus.COMPLETED);

        assertNotNull(response);
        verify(subTaskRepository).findById(subTaskId);
        verify(subTaskRepository).save(subTask);
    }
}