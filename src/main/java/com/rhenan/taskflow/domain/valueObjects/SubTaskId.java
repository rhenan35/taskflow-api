package com.rhenan.taskflow.domain.valueObjects;

import java.util.Objects;
import java.util.UUID;

public record SubTaskId(UUID value) {
    public SubTaskId {
        Objects.requireNonNull(value, "SubTaskId não pode ser nulo!");
    }

    public static SubTaskId newSubTask() {
        return new SubTaskId(UUID.randomUUID());
    }

    public static SubTaskId fromString(String data) {
        return new SubTaskId(UUID.fromString(data));
    }
}
