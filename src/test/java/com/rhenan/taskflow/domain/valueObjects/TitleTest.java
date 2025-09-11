package com.rhenan.taskflow.domain.valueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

    @Test
    void deveCriarTitleValido() {
        Title title = new Title(" Task title ");
        assertEquals("Task title", title.value());
    }

    @Test
    void deveRejeitarTituloVazio() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Title("   "));
        assertTrue(exception.getMessage().toLowerCase().contains("vazio"));
    }

    @Test
    void deveRejeitarTituloMaiorQue200() {
        String big = "a".repeat(201);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Title(big));
        assertTrue(ex.getMessage().toLowerCase().contains("200"));
    }

    @Test
    void deveRejeitarTituloNulo() {
        assertThrows(NullPointerException.class, () -> new Title(null));
    }
}
