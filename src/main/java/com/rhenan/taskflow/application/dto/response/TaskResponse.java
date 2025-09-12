package com.rhenan.taskflow.application.dto.response;

import com.rhenan.taskflow.domain.enums.ActivityStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    UUID userId,
    String title,
    String description,
    ActivityStatus status,
    Instant createdAt,
    Instant completedAt,
    List<SubTaskResponse> subTasks
) {
}