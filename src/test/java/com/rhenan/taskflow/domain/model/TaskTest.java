package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.exception.BusinessRuleException;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.Title;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    void novaTaskDeveComecarPendente() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        assertEquals(ActivityStatus.PENDING, task.getStatus());
        assertNotNull(task.getId());
        assertNotNull(task.getCreatedAt());
        assertNull(task.getCompletedAt());
        assertTrue(task.getSubTask().isEmpty());
    }

    @Test
    void deveAdicionarSubTaskQuandoNaoConcluida() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.addSubTask(new Title("Sub 1"), new Description("desc sub"));
        assertEquals(1, task.getSubTask().size());
        assertEquals("Sub 1", task.getSubTask().getFirst().getTitle().value());
    }

    @Test
    void naoDevePermitirAdicionarSubTaskEmTaskConcluida() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.finish();
        assertThrows(BusinessRuleException.class, () -> task.addSubTask(new Title("Sub proibida"), new Description("desc")));
    }

    @Test
    void devePermitirFinalizarSemSubTasks() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.finish();
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void deveFinalizarTaskComSubTasks() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.addSubTask(new Title("Sub 1"), new Description("desc sub"));
        task.getSubTask().getFirst().updateStatus(ActivityStatus.COMPLETED);

        task.finish();

        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void naoPermiteAlterarStatusDepoisDeFinalizada() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.finish();
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertThrows(BusinessRuleException.class, () -> task.addSubTask(new Title("Tentativa"), new Description("desc")));
    }

    @Test
    void devePermitirTransicaoPendenteParaEmAndamento() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.updateStatus(ActivityStatus.IN_PROGRESS);
        assertEquals(ActivityStatus.IN_PROGRESS, task.getStatus());
        assertNull(task.getCompletedAt());
    }

    @Test
    void devePermitirTransicaoEmAndamentoParaConcluida() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.updateStatus(ActivityStatus.IN_PROGRESS);
        task.updateStatus(ActivityStatus.COMPLETED);
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void devePermitirTransicaoDiretaPendenteParaConcluida() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.updateStatus(ActivityStatus.COMPLETED);
        assertEquals(ActivityStatus.COMPLETED, task.getStatus());
        assertNotNull(task.getCompletedAt());
    }

    @Test
    void naoDevePermitirTransicaoInvalidaUpdateStatus() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.updateStatus(ActivityStatus.IN_PROGRESS);
        assertThrows(BusinessRuleException.class, () -> task.updateStatus(ActivityStatus.PENDING));
    }

    @Test
    void naoDevePermitirAlterarStatusCompletedUpdateStatus() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.updateStatus(ActivityStatus.COMPLETED);
        assertThrows(BusinessRuleException.class, () -> task.updateStatus(ActivityStatus.IN_PROGRESS));
    }
}
