package org.task.task_management_system.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.task.task_management_system.dto.request.CommentRequest;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.entity.Comment;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.service.UserService;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentMapper {
    private final UserService userService;
    private final TaskRepository taskRepository;

    public CommentResponse toResponse(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .taskTitle(comment.getTask().getTitle())
                .userEmail(comment.getUser().getEmail())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .build();
    }

    public Comment toEntity(CommentRequest commentRequest) {

        User user = userService.getUserById(commentRequest.getUserId());


        Task task = taskRepository.findById(commentRequest.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return Comment.builder()
                .content(commentRequest.getContent())
                .user(user)
                .task(task)
                .build();
    }
}