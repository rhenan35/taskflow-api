package com.rhenan.taskflow.application.dto.request;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TaskFilterRequest(
    ActivityStatus status,
    UUID userId,
    String title,
    LocalDateTime createdAfter,
    LocalDateTime createdBefore
) {
    public static TaskFilterRequest empty() {
        return TaskFilterRequest.builder().build();
    }
    
    public boolean hasFilters() {
        return status != null || 
               userId != null || 
               title != null || 
               createdAfter != null || 
               createdBefore != null;
    }
}