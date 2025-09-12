package com.rhenan.taskflow.domain.valueObjects;

import java.util.Objects;

public record Title(String value) {
    public Title {
        Objects.requireNonNull(value, "Title não pode ser nulo!");
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Title não pode ser vazio!");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("Title passou de 200 caracteres");
        }
    }
}
