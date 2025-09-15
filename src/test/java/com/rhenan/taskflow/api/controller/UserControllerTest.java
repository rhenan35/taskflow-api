package com.rhenan.taskflow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhenan.taskflow.application.dto.request.CreateUserRequest;
import com.rhenan.taskflow.application.dto.response.UserResponse;
import com.rhenan.taskflow.application.usecase.user.CreateUserUseCase;
import com.rhenan.taskflow.application.usecase.user.FindUserByIdUseCase;
import com.rhenan.taskflow.application.service.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "joao@email.com"
        );

        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                request.name(),
                request.email()
        );

        when(createUserUseCase.execute(any(CreateUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deveRejeitarUsuarioSemNome() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                null,
                "joao@email.com"
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRejeitarUsuarioSemEmail() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                null
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRejeitarEmailInvalido() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "email-invalido"
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRejeitarNomeVazio() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "",
                "joao@email.com"
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse response = new UserResponse(
                userId,
                "Maria Silva",
                "maria@email.com"
        );

        when(findUserByIdUseCase.execute(eq(userId)))
                .thenReturn(response);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }
}