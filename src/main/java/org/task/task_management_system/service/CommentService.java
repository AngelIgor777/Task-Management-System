package org.task.task_management_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.task_management_system.dto.request.CommentRequest;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.entity.Comment;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.repository.CommentRepository;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.service.mapper.CommentMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.commentMapper = commentMapper;
    }

    // Добавление комментария
    public CommentResponse addComment(CommentRequest commentRequest) {
        Task task = taskRepository.findById(commentRequest.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setTask(task);
        commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    // Получение комментариев для задачи
    public List<CommentResponse> getCommentsByTask(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
