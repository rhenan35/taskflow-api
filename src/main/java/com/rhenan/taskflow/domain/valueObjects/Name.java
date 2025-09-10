package com.rhenan.taskflow.domain.valueObjects;

import java.util.Objects;

public record Name(String value) {
    public Name {
        Objects.requireNonNull(value, "Name não pode ser nulo");
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Name não pode ser vazio");
        }
        if (value.length() > 150) {
            throw new IllegalArgumentException("Name passou de 150 caracteres");
        }
    }
}
