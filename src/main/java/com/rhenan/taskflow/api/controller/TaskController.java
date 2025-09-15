package com.rhenan.taskflow.api.controller;

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
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task-related operations")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CreateSubTaskUseCase createSubTaskUseCase;
    private final FindTasksByStatusUseCase findTasksByStatusUseCase;
    private final FindTasksWithFiltersUseCase findTasksWithFiltersUseCase;
    private final UpdateTaskStatusUseCase updateTaskStatusUseCase;
    private final FindSubTasksByTaskIdUseCase findSubTasksByTaskIdUseCase;

    @GetMapping
    @Operation(summary = "List tasks by status", description = "Lists tasks filtered by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@RequestParam ActivityStatus status) {
        List<TaskResponse> response = findTasksByStatusUseCase.execute(status);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search tasks with filters and pagination", description = "Searches tasks with combined filters and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks found"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PageResponse<TaskResponse>> searchTasks(
            @RequestParam(required = false) ActivityStatus status,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String createdAfter,
            @RequestParam(required = false) String createdBefore,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        TaskFilterRequest filters = TaskFilterRequest.builder()
            .status(status)
            .userId(userId)
            .title(title)
            .createdAfter(createdAfter != null ? java.time.LocalDateTime.parse(createdAfter) : null)
            .createdBefore(createdBefore != null ? java.time.LocalDateTime.parse(createdBefore) : null)
            .build();
        
        PageRequest pageRequest = PageRequest.of(page, size, sortBy, sortDirection);
        
        PageResponse<TaskResponse> response = findTasksWithFiltersUseCase.execute(filters, pageRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a new task in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = createTaskUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{taskId}/subtasks")
    @Operation(summary = "Create subtask", description = "Creates a new subtask for a specific task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subtask created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<SubTaskResponse> createSubTask(@PathVariable UUID taskId, @Valid @RequestBody CreateSubTaskRequest request) {
        SubTaskResponse response = createSubTaskUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{taskId}/subtasks")
    @Operation(summary = "List subtasks of a task", description = "Lists all subtasks of a specific task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtasks found"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<SubTaskResponse>> getSubTasksByTaskId(@PathVariable UUID taskId) {
        List<SubTaskResponse> response = findSubTasksByTaskIdUseCase.execute(taskId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Updates the status of a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID id,
                                                         @Valid @RequestBody UpdateStatusRequest request) {
        TaskResponse response = updateTaskStatusUseCase.execute(id, request.status());
        return ResponseEntity.ok(response);
    }
}