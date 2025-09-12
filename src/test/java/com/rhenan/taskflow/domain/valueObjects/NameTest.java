package com.rhenan.taskflow.domain.valueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NameTest {
    @Test
    void deveCriarNomeValidoAplicandoTrim() {
        Name n = new Name("  Rhenan Coelho  ");
        assertEquals("Rhenan Coelho", n.value());
    }

    @Test
    void deveFalharQuandoNomeForNulo() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    void deveFalharQuandoNomeForVazioOuEspaco() {
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
        assertThrows(IllegalArgumentException.class, () -> new Name("   "));
    }

    @Test
    void deveFalharQuandoNomeUltrapassarLimiteMaximo() {
        String longo = "a".repeat(151);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Name(longo));
        assertTrue(ex.getMessage().toLowerCase().contains("150"));
    }
}
