package com.rhenan.taskflow.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTaskRequest(
    @NotNull(message = "ID do usuário é obrigatório")
    UUID userId,
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 255, message = "Título deve ter entre 1 e 255 caracteres")
    String title,
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    String description
) {
}