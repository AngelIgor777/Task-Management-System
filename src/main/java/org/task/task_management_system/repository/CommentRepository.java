package org.task.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.task_management_system.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
}