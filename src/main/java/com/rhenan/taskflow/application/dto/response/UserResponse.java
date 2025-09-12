package com.rhenan.taskflow.application.dto.response;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email
) {
}