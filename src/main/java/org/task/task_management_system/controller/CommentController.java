package org.task.task_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.task.task_management_system.dto.request.CommentRequest;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Добавление комментария
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.addComment(commentRequest);
        return ResponseEntity.status(201).body(commentResponse);
    }

    // Получение комментариев для задачи
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTask(taskId));
    }
}
