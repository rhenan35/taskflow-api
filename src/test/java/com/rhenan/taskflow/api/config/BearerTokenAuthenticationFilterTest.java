package com.rhenan.taskflow.api.config;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationFilterTest {

    @InjectMocks
    private BearerTokenAuthenticationFilter filter;

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
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token-123");

        filter.doFilterInternal(request, response, filterChain);

        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithInvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer short");

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
        when(request.getHeader("Authorization")).thenReturn("Bearer null");

        assertDoesNotThrow(() -> filter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(request, response);
    }
}