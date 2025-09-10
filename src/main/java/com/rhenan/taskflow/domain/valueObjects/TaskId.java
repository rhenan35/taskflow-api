package com.rhenan.taskflow.domain.valueObjects;

import java.util.Objects;
import java.util.UUID;

public record TaskId(UUID value) {
    public TaskId {
        Objects.requireNonNull(value, "TaskId não pode ser nulo!");
    }

    public static TaskId newTask() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId fromString(String data) {
        return new TaskId(UUID.fromString(data));
    }
}
