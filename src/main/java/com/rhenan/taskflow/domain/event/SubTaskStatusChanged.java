package com.rhenan.taskflow.domain.event;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import com.rhenan.taskflow.domain.valueObjects.SubTaskId;
import com.rhenan.taskflow.domain.valueObjects.TaskId;

import java.time.Instant;
import java.util.Objects;

public record SubTaskStatusChanged(TaskId taskId,
                                   SubTaskId subTaskId,
                                   ActivityStatus from,
                                   ActivityStatus to,
                                   Instant occurredAt) implements DomainEvent {
    public SubTaskStatusChanged {
        Objects.requireNonNull(taskId);
        Objects.requireNonNull(subTaskId);
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(occurredAt);
    }

    public static SubTaskStatusChanged now(TaskId taskId, SubTaskId subTaskId, ActivityStatus from, ActivityStatus to) {
        return new SubTaskStatusChanged(taskId, subTaskId, from, to, Instant.now());
    }
}
