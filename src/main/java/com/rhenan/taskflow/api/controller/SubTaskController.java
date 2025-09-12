package com.rhenan.taskflow.api.controller;

import com.rhenan.taskflow.application.dto.request.PageRequest;
import com.rhenan.taskflow.application.dto.request.SubTaskFilterRequest;
import com.rhenan.taskflow.application.dto.request.UpdateStatusRequest;
import com.rhenan.taskflow.application.dto.response.PageResponse;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;
import com.rhenan.taskflow.application.usecase.subtask.FindSubTasksWithFiltersUseCase;
import com.rhenan.taskflow.application.usecase.subtask.UpdateSubTaskStatusUseCase;
import com.rhenan.taskflow.domain.enums.ActivityStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/subtarefas")
@RequiredArgsConstructor
@Tag(name = "SubTasks", description = "Operações relacionadas às subtarefas")
@SecurityRequirement(name = "bearerAuth")
public class SubTaskController {

    private final UpdateSubTaskStatusUseCase updateSubTaskStatusUseCase;
    private final FindSubTasksWithFiltersUseCase findSubTasksWithFiltersUseCase;
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da subtarefa", description = "Atualiza o status de uma subtarefa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Subtarefa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<SubTaskResponse> updateSubTaskStatus(@PathVariable UUID id,
                                                               @Valid @RequestBody UpdateStatusRequest request) {
        SubTaskResponse response = updateSubTaskStatusUseCase.execute(id, request.status());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/busca")
    @Operation(summary = "Buscar subtarefas com filtros e paginação", description = "Busca subtarefas com filtros combinados e paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtarefas encontradas"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<PageResponse<SubTaskResponse>> searchSubTasks(
            @RequestParam(required = false) ActivityStatus status,
            @RequestParam(required = false) UUID taskId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String createdAfter,
            @RequestParam(required = false) String createdBefore,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        SubTaskFilterRequest filters = SubTaskFilterRequest.builder()
            .status(status)
            .taskId(taskId)
            .title(title)
            .createdAfter(createdAfter != null ? java.time.LocalDateTime.parse(createdAfter) : null)
            .createdBefore(createdBefore != null ? java.time.LocalDateTime.parse(createdBefore) : null)
            .build();
        
        PageRequest pageRequest = PageRequest.of(page, size, sortBy, sortDirection);
        
        PageResponse<SubTaskResponse> response = findSubTasksWithFiltersUseCase.execute(filters, pageRequest);
        return ResponseEntity.ok(response);
    }
}