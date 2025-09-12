package com.rhenan.taskflow.api.controller;

import com.rhenan.taskflow.application.dto.request.UpdateStatusRequest;
import com.rhenan.taskflow.application.dto.response.SubTaskResponse;

import com.rhenan.taskflow.application.usecase.subtask.UpdateSubTaskStatusUseCase;
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




}