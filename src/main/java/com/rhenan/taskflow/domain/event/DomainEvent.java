package com.rhenan.taskflow.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
