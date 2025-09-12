package com.rhenan.taskflow.domain.valueObjects;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "UserId não pode ser nulo!");
    }

    public static UserId newUser() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String data) {
        return new UserId(UUID.fromString(data));
    }
}
