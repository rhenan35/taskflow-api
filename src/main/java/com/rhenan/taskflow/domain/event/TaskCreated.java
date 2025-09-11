package com.rhenan.taskflow.domain.event;

import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.time.Instant;
import java.util.Objects;

public record TaskCreated(TaskId taskId, UserId ownerId, Instant occurredAt) implements DomainEvent {
    public TaskCreated {
        Objects.requireNonNull(taskId);
        Objects.requireNonNull(ownerId);
        Objects.requireNonNull(occurredAt);
    }

    public static TaskCreated now(TaskId taskId, UserId ownerId) {
        return new TaskCreated(taskId, ownerId, Instant.now());
    }
}
