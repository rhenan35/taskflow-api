package com.rhenan.taskflow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.request.CreateTaskRequest;
import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.TaskFilterRequest;
import com.rhenan.taskflow.application.dto.request.UpdateStatusRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.dto.response.TaskResponse;
import com.rhenan.taskflow.application.usecase.task.CreateTaskUseCase;
import com.rhenan.taskflow.application.usecase.task.FindTasksByStatusUseCase;
import com.rhenan.taskflow.application.usecase.task.FindTasksWithFiltersUseCase;
import com.rhenan.taskflow.application.usecase.task.UpdateTaskStatusUseCase;
import com.rhenan.taskflow.application.usecase.subtask.CreateSubTaskUseCase;
import com.rhenan.taskflow.application.usecase.subtask.FindSubTasksByTaskIdUseCase;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTaskUseCase createTaskUseCase;

    @MockitoBean
    private CreateSubTaskUseCase createSubTaskUseCase;

    @MockitoBean
    private FindTasksByStatusUseCase findTasksByStatusUseCase;

    @MockitoBean
    private FindTasksWithFiltersUseCase findTasksWithFiltersUseCase;

    @MockitoBean
    private UpdateTaskStatusUseCase updateTaskStatusUseCase;

    @MockitoBean
    private FindSubTasksByTaskIdUseCase findSubTasksByTaskIdUseCase;

    @Test
    void deveListarTarefasPorStatus() throws Exception {
        TaskResponse taskResponse = new TaskResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Tarefa Teste",
                "Descrição",
                ActivityStatus.PENDING,
                Instant.now(),
                null,
                List.of()
        );

        when(findTasksByStatusUseCase.execute(ActivityStatus.PENDING))
                .thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/tarefas")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Tarefa Teste"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void deveCriarTarefaComSucesso() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                UUID.randomUUID(),
                "Nova Tarefa",
                "Descrição da tarefa"
        );

        TaskResponse response = new TaskResponse(
                UUID.randomUUID(),
                request.userId(),
                request.title(),
                request.description(),
                ActivityStatus.PENDING,
                Instant.now(),
                null,
                List.of()
        );

        when(createTaskUseCase.execute(any(CreateTaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nova Tarefa"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void deveRejeitarTarefaSemTitulo() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                UUID.randomUUID(),
                null,
                "Descrição"
        );

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveCriarSubTarefaComSucesso() throws Exception {
        UUID tarefaId = UUID.randomUUID();
        CreateSubTaskRequest request = new CreateSubTaskRequest(
                tarefaId,
                "Nova SubTarefa",
                "Descrição da subtarefa"
        );

        SubTaskResponse response = new SubTaskResponse(
                UUID.randomUUID(),
                tarefaId,
                request.title(),
                request.description(),
                ActivityStatus.PENDING,
                Instant.now(),
                null
        );

        when(createSubTaskUseCase.execute(any(CreateSubTaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/tarefas/{tarefaId}/subtarefas", tarefaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nova SubTarefa"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void deveListarSubTarefasPorTarefaId() throws Exception {
        UUID tarefaId = UUID.randomUUID();
        SubTaskResponse subTaskResponse = new SubTaskResponse(
                UUID.randomUUID(),
                tarefaId,
                "SubTarefa Teste",
                "Descrição",
                ActivityStatus.PENDING,
                Instant.now(),
                null
        );

        when(findSubTasksByTaskIdUseCase.execute(tarefaId))
                .thenReturn(List.of(subTaskResponse));

        mockMvc.perform(get("/tarefas/{tarefaId}/subtarefas", tarefaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("SubTarefa Teste"))
                .andExpect(jsonPath("$[0].taskId").value(tarefaId.toString()));
    }

    @Test
    void deveAtualizarStatusDaTarefa() throws Exception {
        UUID taskId = UUID.randomUUID();
        UpdateStatusRequest request = new UpdateStatusRequest(ActivityStatus.COMPLETED);

        TaskResponse response = new TaskResponse(
                taskId,
                UUID.randomUUID(),
                "Tarefa Atualizada",
                "Descrição",
                ActivityStatus.COMPLETED,
                Instant.now(),
                Instant.now(),
                List.of()
        );

        when(updateTaskStatusUseCase.execute(eq(taskId), eq(ActivityStatus.COMPLETED)))
                .thenReturn(response);

        mockMvc.perform(patch("/tarefas/{id}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.completedAt").exists());
    }

    @Test
    void deveBuscarTarefasComFiltrosEPaginacao() throws Exception {
        UUID userId = UUID.randomUUID();
        TaskResponse taskResponse = new TaskResponse(
                UUID.randomUUID(),
                userId,
                "Tarefa Filtrada",
                "Descrição",
                ActivityStatus.PENDING,
                Instant.now(),
                null,
                List.of()
        );

        PageResponse<TaskResponse> pageResponse = PageResponse.of(
                List.of(taskResponse),
                0,
                10,
                1L
        );

        when(findTasksWithFiltersUseCase.execute(any(TaskFilterRequest.class), any(PageRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/tarefas/search")
                        .param("status", "PENDING")
                        .param("userId", userId.toString())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Tarefa Filtrada"))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarTarefasSemFiltros() throws Exception {
        TaskResponse taskResponse = new TaskResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Tarefa Sem Filtro",
                "Descrição",
                ActivityStatus.IN_PROGRESS,
                Instant.now(),
                null,
                List.of()
        );

        PageResponse<TaskResponse> pageResponse = PageResponse.of(
                List.of(taskResponse),
                0,
                10,
                1L
        );

        when(findTasksWithFiltersUseCase.execute(any(TaskFilterRequest.class), any(PageRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/tarefas/search")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Tarefa Sem Filtro"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarTarefasComFiltroDeData() throws Exception {
        TaskResponse taskResponse = new TaskResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Tarefa Recente",
                "Descrição",
                ActivityStatus.COMPLETED,
                Instant.now(),
                Instant.now(),
                List.of()
        );

        PageResponse<TaskResponse> pageResponse = PageResponse.of(
                List.of(taskResponse),
                0,
                5,
                1L
        );

        when(findTasksWithFiltersUseCase.execute(any(TaskFilterRequest.class), any(PageRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/tarefas/search")
                        .param("createdAfter", "2024-01-01T00:00:00")
                        .param("createdBefore", "2024-12-31T23:59:59")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Tarefa Recente"))
                .andExpect(jsonPath("$.size").value(5));
    }

    @Test
    void deveRejeitarStatusInvalido() throws Exception {
        UUID taskId = UUID.randomUUID();
        String invalidJson = "{\"status\":\"INVALID_STATUS\"}";

        mockMvc.perform(patch("/tarefas/{id}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}