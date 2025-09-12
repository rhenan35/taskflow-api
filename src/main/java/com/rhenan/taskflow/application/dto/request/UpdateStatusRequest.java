package com.rhenan.taskflow.application.dto.request;

import com.rhenan.taskflow.domain.enums.ActivityStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
    @NotNull(message = "Status é obrigatório")
    ActivityStatus status
) {
}