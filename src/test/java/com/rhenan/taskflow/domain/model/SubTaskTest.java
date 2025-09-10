package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.valueObjects.Description;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {
    @Test
    void novaSubTaskComecaPendente() {
        SubTask st = SubTask.newSubTask(TaskId.newTask(), new Title("T1"), new Description("Desc"));
        assertEquals(ActivityStatus.PENDING, st.getStatus());
        assertNotNull(st.getCreatedAt());
        assertNull(st.getCompletedAt());
        assertEquals("T1", st.getTitle().value());
        assertNotNull(st.getTaskId());
        assertNotNull(st.getId());
    }

    @Test
    void podeIrDePendenteParaEmAndamentoEDepoisParaConcluida() {
        SubTask st = SubTask.newSubTask(TaskId.newTask(), new Title("T1"), null);

        st.updateStatus(ActivityStatus.IN_PROGRESS);
        assertEquals(ActivityStatus.IN_PROGRESS, st.getStatus());
        assertNull(st.getCompletedAt());

        st.updateStatus(ActivityStatus.COMPLETED);
        assertEquals(ActivityStatus.COMPLETED, st.getStatus());
        assertNotNull(st.getCompletedAt());
    }

    @Test
    void naoPermiteTransicaoInvalida() {
        SubTask st = SubTask.newSubTask(TaskId.newTask(), new Title("T1"), null);
        st.updateStatus(ActivityStatus.IN_PROGRESS);
        assertThrows(IllegalStateException.class, () -> st.updateStatus(ActivityStatus.PENDING));
    }

    @Test
    void naoPermiteAlterarDepoisDeConcluida() {
        SubTask st = SubTask.newSubTask(TaskId.newTask(), new Title("T1"), null);
        st.updateStatus(ActivityStatus.COMPLETED);
        assertThrows(IllegalStateException.class, () -> st.updateStatus(ActivityStatus.IN_PROGRESS));
    }

    @Test
    void idsDevemSerImutaveis() {
        SubTask st = SubTask.newSubTask(TaskId.newTask(), new Title("T1"), new Description("x"));
        SubTaskId id1 = st.getId();
        SubTaskId id2 = st.getId();
        assertSame(id1, id2);
        assertEquals(id1, id2);
    }
}
