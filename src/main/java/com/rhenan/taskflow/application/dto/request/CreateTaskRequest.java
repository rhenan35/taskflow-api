package com.rhenan.taskflow.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTaskRequest(
    @NotNull(message = "ID do usuário é obrigatório")
    UUID userId,
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    String title,
    
    @Size(max = 1000, message = "Description must have at most 1000 characters")
    String description
) {
}