package org.task.task_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.task.task_management_system.dto.request.CommentRequest;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.service.CommentService;

@Tag(name = "Comment Controller", description = "API for working with task comments")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add a new comment",
            description = "Adds a comment to a specific task",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comment added successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
            })
    @PreAuthorize("@taskSecurityService.canChangeOrWatch(#commentRequest.taskId, @jwtService.extractToken())")
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.addComment(commentRequest);
        return ResponseEntity.status(201).body(commentResponse);
    }

    @Operation(summary = "Get comments for a task",
            description = "Retrieves all comments for a specific task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
            })
    @PreAuthorize("@taskSecurityService.canChangeOrWatch(#taskId, @jwtService.extractToken())")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentResponse>> getCommentsByTask(@PathVariable Long taskId, Pageable pageable) {
        Page<CommentResponse> comments = commentService.getCommentsByTask(taskId, pageable);
        return ResponseEntity.ok(comments);
    }
}
