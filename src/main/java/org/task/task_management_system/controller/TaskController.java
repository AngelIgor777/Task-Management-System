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

    @Operation(summary = "Get task by ID",
            description = "Retrieves a specific task by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task retrieved successfully")
            })
    @PreAuthorize("@taskSecurityService.canChangeOrWatch(#taskId, @jwtService.extractToken())")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId) {
        TaskResponse task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(Pageable pageable,
                                                          @RequestParam(required = false) String status,
                                                          @RequestParam(required = false) String priority,
                                                          @RequestParam(required = false) Long authorId,
                                                          @RequestParam(required = false) Long assigneeId) {
        Page<TaskResponse> tasks = taskService.getTasks(pageable, status, priority, authorId, assigneeId);
        return ResponseEntity.ok(tasks);
    }


    @Operation(summary = "Update task status and priority",
            description = "Updates the status and priority of a task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated successfully")
            })
    @PreAuthorize("@taskSecurityService.canChangeOrWatch(#taskId, @jwtService.extractToken())")
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTaskStatusAndPriority(@PathVariable Long taskId,
                                                                    @RequestParam String status,
                                                                    @RequestParam String priority) {
        TaskResponse updatedTask = taskService.updateTaskStatusAndPriority(taskId, status, priority);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Delete a task",
            description = "Deletes a task by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task deleted successfully")
            })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
