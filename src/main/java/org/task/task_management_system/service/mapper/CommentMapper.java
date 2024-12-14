package org.task.task_management_system.service.mapper;

import org.springframework.stereotype.Component;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.entity.Comment;

import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setTaskId(comment.getTask().getId());
        response.setTaskTitle(comment.getTask().getTitle());
        response.setUserEmail(comment.getUser().getEmail());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
}