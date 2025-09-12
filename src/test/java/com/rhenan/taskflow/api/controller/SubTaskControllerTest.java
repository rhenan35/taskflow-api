package com.rhenan.taskflow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhenan.taskflow.application.dto.request.UpdateStatusRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.usecase.subtask.DeleteSubTaskUseCase;
import com.rhenan.taskflow.application.usecase.subtask.UpdateSubTaskStatusUseCase;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubTaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UpdateSubTaskStatusUseCase updateSubTaskStatusUseCase;

    @MockitoBean
    private DeleteSubTaskUseCase deleteSubTaskUseCase;

    @Test
    void deveAtualizarStatusDaSubTarefa() throws Exception {
        UUID subTaskId = UUID.randomUUID();
        UpdateStatusRequest request = new UpdateStatusRequest(ActivityStatus.COMPLETED);

        SubTaskResponse response = new SubTaskResponse(
                subTaskId,
                UUID.randomUUID(),
                "SubTarefa Atualizada",
                "Descrição",
                ActivityStatus.COMPLETED,
                Instant.now(),
                Instant.now()
        );

        when(updateSubTaskStatusUseCase.execute(eq(subTaskId), eq(ActivityStatus.COMPLETED)))
                .thenReturn(response);

        mockMvc.perform(patch("/subtarefas/{id}/status", subTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.completedAt").exists());
    }

    @Test
    void deveRejeitarStatusInvalidoParaSubTarefa() throws Exception {
        UUID subTaskId = UUID.randomUUID();
        String invalidJson = "{\"status\":\"INVALID_STATUS\"}";

        mockMvc.perform(patch("/subtarefas/{id}/status", subTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }



    @Test
    void deveAtualizarStatusParaPendente() throws Exception {
        UUID subTaskId = UUID.randomUUID();
        UpdateStatusRequest request = new UpdateStatusRequest(ActivityStatus.PENDING);

        SubTaskResponse response = new SubTaskResponse(
                subTaskId,
                UUID.randomUUID(),
                "SubTarefa Pendente",
                "Descrição",
                ActivityStatus.PENDING,
                Instant.now(),
                null
        );

        when(updateSubTaskStatusUseCase.execute(eq(subTaskId), eq(ActivityStatus.PENDING)))
                .thenReturn(response);

        mockMvc.perform(patch("/subtarefas/{id}/status", subTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.completedAt").doesNotExist());
    }

    @Test
    void deveAtualizarStatusParaEmProgresso() throws Exception {
        UUID subTaskId = UUID.randomUUID();
        UpdateStatusRequest request = new UpdateStatusRequest(ActivityStatus.IN_PROGRESS);

        SubTaskResponse response = new SubTaskResponse(
                subTaskId,
                UUID.randomUUID(),
                "SubTarefa Em Progresso",
                "Descrição",
                ActivityStatus.IN_PROGRESS,
                Instant.now(),
                null
        );

        when(updateSubTaskStatusUseCase.execute(eq(subTaskId), eq(ActivityStatus.IN_PROGRESS)))
                .thenReturn(response);

        mockMvc.perform(patch("/subtarefas/{id}/status", subTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.completedAt").doesNotExist());
    }
}