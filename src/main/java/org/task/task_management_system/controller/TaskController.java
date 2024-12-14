package org.task.task_management_system.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.task.task_management_system.dto.request.TaskRequest;
import org.task.task_management_system.dto.response.TaskResponse;
import org.task.task_management_system.service.TaskService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task Controller", description = "API for working with tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task",
            description = "Creates a new task in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created successfully"),
            })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.status(201).body(taskResponse);
    }

    @Operation(summary = "Get tasks by assignee",
            description = "Retrieves tasks assigned to a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            })
    @PreAuthorize("@taskSecurityService.canChangeOrWatch(@jwtService.extractToken())")
    @GetMapping("/assignee/{assigneeName}")
    public ResponseEntity<Page<TaskResponse>> getTasksByAssigneeName(@PathVariable String assigneeName, Pageable pageable) {
        Page<TaskResponse> tasks = taskService.getTasksByAssigneeName(assigneeName, pageable);
        return ResponseEntity.ok(tasks);
    }

}
