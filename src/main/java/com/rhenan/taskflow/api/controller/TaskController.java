package com.rhenan.taskflow.api.controller;

import com.rhenan.taskflow.application.dto.request.CreateSubTaskRequest;
import com.rhenan.taskflow.application.dto.request.CreateTaskRequest;
import com.rhenan.taskflow.application.dto.request.UpdateStatusRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.dto.response.TaskResponse;

import com.rhenan.taskflow.application.usecase.task.CreateTaskUseCase;
import com.rhenan.taskflow.application.usecase.task.FindTasksByStatusUseCase;
import com.rhenan.taskflow.application.usecase.task.UpdateTaskStatusUseCase;
import com.rhenan.taskflow.application.usecase.subtask.CreateSubTaskUseCase;
import com.rhenan.taskflow.application.usecase.subtask.FindSubTasksByTaskIdUseCase;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Operações relacionadas às tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CreateSubTaskUseCase createSubTaskUseCase;
    private final FindTasksByStatusUseCase findTasksByStatusUseCase;
    private final UpdateTaskStatusUseCase updateTaskStatusUseCase;

    private final FindSubTasksByTaskIdUseCase findSubTasksByTaskIdUseCase;

    @GetMapping
    @Operation(summary = "Listar tarefas por status", description = "Lista tarefas filtradas por status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefas encontradas"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@RequestParam ActivityStatus status) {
        List<TaskResponse> response = findTasksByStatusUseCase.execute(status);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = createTaskUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{tarefaId}/subtarefas")
    @Operation(summary = "Criar subtarefa", description = "Cria uma nova subtarefa para uma tarefa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subtarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<SubTaskResponse> createSubTask(@PathVariable UUID tarefaId, @Valid @RequestBody CreateSubTaskRequest request) {
        SubTaskResponse response = createSubTaskUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{tarefaId}/subtarefas")
    @Operation(summary = "Listar subtarefas de uma tarefa", description = "Lista todas as subtarefas de uma tarefa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtarefas encontradas"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<SubTaskResponse>> getSubTasksByTaskId(@PathVariable UUID tarefaId) {
        List<SubTaskResponse> response = findSubTasksByTaskIdUseCase.execute(tarefaId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da tarefa", description = "Atualiza o status de uma tarefa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID id,
                                                         @Valid @RequestBody UpdateStatusRequest request) {
        TaskResponse response = updateTaskStatusUseCase.execute(id, request.status());
        return ResponseEntity.ok(response);
    }
}