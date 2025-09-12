package com.rhenan.taskflow.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String name,
    
    @Email(message = "Email deve ter formato válido")
    String email
) {
}