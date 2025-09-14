package com.rhenan.taskflow.application.usecase.auth;

import com.rhenan.taskflow.application.dto.request.LoginRequest;
import com.rhenan.taskflow.application.dto.response.LoginResponse;
import com.rhenan.taskflow.application.service.JwtTokenService;
import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final JwtTokenService jwtTokenService;
    
    public LoginResponse authenticate(LoginRequest request) {
        final String DEMO_EMAIL = "admin@taskflow.com";
        final String DEMO_PASSWORD = "taskflow123";
        
        if (!DEMO_EMAIL.equals(request.getEmail()) || !DEMO_PASSWORD.equals(request.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        
        UserId demoUserId = UserId.newUser();
        Email demoEmail = new Email(DEMO_EMAIL);
        
        String token = jwtTokenService.generateToken(demoUserId.value(), demoEmail.value());
        
        return new LoginResponse(token, demoUserId.value(), demoEmail.value());
    }
}