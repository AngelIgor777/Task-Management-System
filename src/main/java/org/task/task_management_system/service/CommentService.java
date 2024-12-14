package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.task_management_system.dto.request.CommentRequest;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.entity.Comment;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.repository.CommentRepository;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.repository.UserRepository;
import org.task.task_management_system.security.TaskSecurityService;
import org.task.task_management_system.service.mapper.CommentMapper;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;


    // Добавление комментария
    public CommentResponse addComment(CommentRequest commentRequest) {
        Task task = taskRepository.findById(commentRequest.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setTask(task);
        comment.setUser(user);

        commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    // Получение комментариев с пагинацией для задачи
    public Page<CommentResponse> getCommentsByTask(Long taskId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByTaskId(taskId, pageable);
        return comments.map(commentMapper::toResponse);
    }




    // Получение конкретного комментария по ID
    public CommentResponse getCommentById(Long commentId, Long taskId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getId().equals(taskId))
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return commentMapper.toResponse(comment);
    }

    // Обновление комментария
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Проверяем, что комментарий относится к той же задаче
        if (!comment.getTask().getId().equals(commentRequest.getTaskId())) {
            throw new RuntimeException("Comment does not belong to the provided task");
        }

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    // Удаление комментария
    public void deleteComment(Long commentId, Long taskId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getId().equals(taskId))
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }
}
