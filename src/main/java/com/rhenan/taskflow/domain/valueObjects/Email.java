package com.rhenan.taskflow.domain.valueObjects;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        Objects.requireNonNull(value, "Email não pode ser nulo");

        String normalized = value.trim().toLowerCase();

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (!EMAIL_REGEX.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Email inválido: " + value);
        }

        value = normalized;
    }

    public static Email de(String raw) {
        return new Email(raw);
    }
}
