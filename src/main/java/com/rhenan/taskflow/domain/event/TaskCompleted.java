package com.rhenan.taskflow.domain.event;

import com.rhenan.taskflow.domain.valueObjects.TaskId;

import java.time.Instant;
import java.util.Objects;

public record TaskCompleted(TaskId taskId, Instant occurredAt) implements DomainEvent {
    public TaskCompleted {
        Objects.requireNonNull(taskId);
        Objects.requireNonNull(occurredAt);
    }

    public static TaskCompleted now(TaskId taskId) {
        return new TaskCompleted(taskId, Instant.now());
    }
}
