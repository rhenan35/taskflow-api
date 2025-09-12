package com.rhenan.taskflow.application.dto.response;

import com.rhenan.taskflow.domain.enums.ActivityStatus;

import java.time.Instant;
import java.util.UUID;

public record SubTaskResponse(
    UUID id,
    UUID taskId,
    String title,
    String description,
    ActivityStatus status,
    Instant createdAt,
    Instant completedAt
) {
}