package com.rhenan.taskflow.api.controller;

import com.rhenan.taskflow.application.dto.request.LoginRequest;
import com.rhenan.taskflow.application.dto.response.LoginResponse;
import com.rhenan.taskflow.application.usecase.auth.AuthenticateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {
    
    private final AuthenticateUserUseCase authenticateUserUseCase;
    
    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticateUserUseCase.authenticate(request);
        return ResponseEntity.ok(response);
    }
}