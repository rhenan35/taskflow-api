package com.rhenan.taskflow.api.config;

import com.rhenan.taskflow.application.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationFilterTest {

    @InjectMocks
    private BearerTokenAuthenticationFilter filter;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldAuthenticateWithValidBearerToken() throws ServletException, IOException {
        String token = "valid-token-123";
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenService.validateToken(token)).thenReturn(true);
        when(jwtTokenService.extractUserId(token)).thenReturn(userId);
        when(jwtTokenService.extractEmail(token)).thenReturn(email);

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithInvalidToken() throws ServletException, IOException {
        String token = "short";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenService.validateToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithoutBearerPrefix() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic valid-token-123");

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithoutAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithEmptyToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithOnlyBearerKeyword() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer");

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldHandleNullTokenGracefully() throws ServletException, IOException {
        String token = "null";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenService.validateToken(token)).thenReturn(false);

        assertDoesNotThrow(() -> filter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(request, response);
    }
}