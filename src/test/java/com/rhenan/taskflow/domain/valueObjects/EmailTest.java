package com.rhenan.taskflow.domain.valueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTest {
    @Test
    void deveCriarEmailValidoComTrimELowercase() {
        Email e = new Email("  John.Doe@Example.COM  ");
        assertEquals("john.doe@example.com", e.value());
    }

    @Test
    void deveFalharQuandoEmailForNulo() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    void deveFalharQuandoEmailForVazioOuEspaco() {
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertThrows(IllegalArgumentException.class, () -> new Email("   "));
    }

    @Test
    void deveFalharQuandoNaoTiverArrobaOuDominio() {
        assertThrows(IllegalArgumentException.class, () -> new Email("john.doeexample.com"));
        assertThrows(IllegalArgumentException.class, () -> new Email("john@"));
        assertThrows(IllegalArgumentException.class, () -> new Email("@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new Email("john@.com"));
    }
}
