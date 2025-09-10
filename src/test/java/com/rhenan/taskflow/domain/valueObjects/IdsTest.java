package com.rhenan.taskflow.domain.valueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdsTest {
    @Test
    void usuarioIdNovoENaoNulo() {
        UserId id = UserId.newUser();
        assertNotNull(id);
        assertNotNull(id.value());
    }

    @Test
    void taskIdNovoENaoNulo() {
        TaskId id = TaskId.newTask();
        assertNotNull(id);
        assertNotNull(id.value());
    }

    @Test
    void subTaskIdNovoENaoNulo() {
        SubTaskId id = SubTaskId.newSubTask();
        assertNotNull(id);
        assertNotNull(id.value());
    }

    @Test
    void deveConverterDeStringValida() {
        String raw = TaskId.newTask().value().toString();
        TaskId parsed = TaskId.fromString(raw);
        assertEquals(raw, parsed.value().toString());
    }

    @Test
    void deveFalharAoConverterStringInvalida() {
        assertThrows(IllegalArgumentException.class, () -> TaskId.fromString("not-an-uuid"));
    }
}
