package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;

import com.rhenan.taskflow.domain.valueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    void novaTaskDeveComecarPendente() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        assertEquals(ActivityStatus.PENDING, task.getStatus());
        assertNotNull(task.getId());
        assertNotNull(task.getCreatedAt());
        assertNull(task.getCompletedAt());
        assertTrue(task.getSubTask().isEmpty());
    }

    @Test
    void deveAdicionarSubTaskQuandoNaoConcluida() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.addSubTask("Sub 1", "desc sub");
        assertEquals(1, task.getSubTask().size());
        assertEquals("Sub 1", task.getSubTask().getFirst().getTitle());
    }

    @Test
    void naoDevePermitirAdicionarSubTaskEmTaskConcluida() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.finish();
        assertThrows(BusinessRuleException.class, () -> task.addSubTask("Sub proibida", "desc"));
    }

    @Test
    void devePermitirFinalizarSemSubTasks() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.finish();
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void deveFinalizarTaskComSubTasks() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.addSubTask("Sub 1", "desc sub");
        task.getSubTask().getFirst().updateStatus(ActivityStatus.COMPLETED);

        task.finish();

        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void naoDeveFinalizarTaskComSubTasksPendentes() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.addSubTask("Sub 1", "desc sub");
        task.addSubTask("Sub 2", "desc sub 2");
        
        task.getSubTask().getFirst().updateStatus(ActivityStatus.COMPLETED);
        
        assertThrows(BusinessRuleException.class, () -> task.finish());
        assertEquals(ActivityStatus.PENDING, task.getStatus());
        assertNull(task.getCompletedAt());
    }

    @Test
    void naoPermiteAlterarStatusDepoisDeFinalizada() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.finish();
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertThrows(BusinessRuleException.class, () -> task.addSubTask("Tentativa", "desc"));
    }

    @Test
    void devePermitirTransicaoPendenteParaEmAndamento() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.updateStatus(ActivityStatus.IN_PROGRESS);
        assertEquals(ActivityStatus.IN_PROGRESS, task.getStatus());
        assertNull(task.getCompletedAt());
    }

    @Test
    void devePermitirTransicaoEmAndamentoParaConcluida() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.updateStatus(ActivityStatus.IN_PROGRESS);
        task.updateStatus(ActivityStatus.COMPLETED);
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void devePermitirTransicaoDiretaPendenteParaConcluida() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.updateStatus(ActivityStatus.COMPLETED);
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void naoDevePermitirTransicaoInvalidaUpdateStatus() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.updateStatus(ActivityStatus.IN_PROGRESS);
        assertThrows(BusinessRuleException.class, () -> task.updateStatus(ActivityStatus.PENDING));
    }

    @Test
    void naoDevePermitirAlterarStatusCompletedUpdateStatus() {
        Task task = Task.createTask(UserId.newUser(), "Tarefa 1", "desc");
        task.updateStatus(ActivityStatus.COMPLETED);
        assertThrows(BusinessRuleException.class, () -> task.updateStatus(ActivityStatus.IN_PROGRESS));
    }
}
