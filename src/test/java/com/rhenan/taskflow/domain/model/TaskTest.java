package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
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
        task.addSubTask(new Title("Sub 1"));
        assertEquals(1, task.getSubTask().size());
        assertEquals("Sub 1", task.getSubTask().getFirst().getTitle().value());
    }

    @Test
    void naoDevePermitirAdicionarSubTaskEmTaskConcluida() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.finish(); // sem subtasks → regra ainda aceita terminar direto
        assertThrows(IllegalStateException.class, () -> task.addSubTask(new Title("Sub proibida")));
    }

    @Test
    void naoDevePermitirFinalizarComSubTaskPendente() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.addSubTask(new Title("Sub 1"));
        assertThrows(IllegalStateException.class, task::finish);
    }

    @Test
    void deveFinalizarTaskComTodasSubTasksConcluidas() {
        Task task = Task.createTask(UserId.newUser(), new Title("Tarefa 1"), new Description("desc"));
        task.addSubTask(new Title("Sub 1"));
        task.getSubTask().getFirst().updateStatus(ActivityStatus.IN_PROGRESS);
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
        assertThrows(IllegalStateException.class, () -> task.addSubTask(new Title("Tentativa")));
    }
}
