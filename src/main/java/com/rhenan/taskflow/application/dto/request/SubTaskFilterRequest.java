package com.rhenan.taskflow.application.dto.request;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SubTaskFilterRequest(
    ActivityStatus status,
    UUID taskId,
    String title,
    LocalDateTime createdAfter,
    LocalDateTime createdBefore
) {
}