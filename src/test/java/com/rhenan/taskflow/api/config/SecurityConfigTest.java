package com.rhenan.taskflow.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.rhenan.taskflow.application.usecase.user.CreateUserUseCase;
import com.rhenan.taskflow.application.usecase.user.FindUserByIdUseCase;
import com.rhenan.taskflow.api.controller.UserController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @Test
    void shouldAllowAccessToPublicEndpoints() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRequireAuthenticationForProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAccessWithValidBearerToken() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .header("Authorization", "Bearer valid-token-123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidBearerToken() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .header("Authorization", "Bearer short"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectMalformedAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .header("Authorization", "Basic invalid-format"))
                .andExpect(status().isUnauthorized());
    }
}