package com.rhenan.taskflow.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.Instant;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            Map<String, Object> errorResponse = Map.of(
                "timestamp", Instant.now(),
                "status", 403,
                "error", "Forbidden",
                "message", "Acesso negado. Verifique o token JWT."
            );
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authConfig = http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint()))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/docs/**", "/api-docs/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/api/auth/login").permitAll();
                
                if ("dev".equals(activeProfile)) {
                    auth.requestMatchers("/h2-console/**").permitAll();
                }
                
                auth.anyRequest().authenticated();
            });
        
        if ("dev".equals(activeProfile)) {
            authConfig.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        }
        
        authConfig.addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return authConfig.build();
    }
}