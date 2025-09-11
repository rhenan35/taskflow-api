package com.rhenan.taskflow.domain.event;

import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;
import com.rhenan.taskflow.domain.valueObjects.Title;

import java.time.Instant;
import java.util.Objects;

public record SubTaskAdded(TaskId taskId, SubTaskId subTaskId, Title title, Instant occurredAt) implements DomainEvent {
    public SubTaskAdded {
        Objects.requireNonNull(taskId);
        Objects.requireNonNull(subTaskId);
        Objects.requireNonNull(title);
        Objects.requireNonNull(occurredAt);
    }

    public static SubTaskAdded now(TaskId taskId, SubTaskId subTaskId, Title title) {
        return new SubTaskAdded(taskId, subTaskId, title, Instant.now());
    }
}
