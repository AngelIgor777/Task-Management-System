package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.task_management_system.dto.request.CommentRequest;
import org.task.task_management_system.dto.response.CommentResponse;
import org.task.task_management_system.entity.Comment;
import org.task.task_management_system.repository.CommentRepository;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.repository.UserRepository;
import org.task.task_management_system.service.mapper.CommentMapper;

import javax.persistence.EntityNotFoundException;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;



    public CommentResponse addComment(CommentRequest commentRequest) {
        Comment comment = commentMapper.toEntity(commentRequest);
        comment = commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    public Page<CommentResponse> getCommentsByTask(Long taskId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByTaskId(taskId, pageable);
        return comments.map(commentMapper::toResponse);
    }


    public CommentResponse getCommentById(Long commentId, Long taskId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getId().equals(taskId))
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return commentMapper.toResponse(comment);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getTask().getId().equals(commentRequest.getTaskId())) {
            throw new IllegalArgumentException("Comment does not belong to the provided task");
        }

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    public void deleteComment(Long commentId, Long taskId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getId().equals(taskId))
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }
}
