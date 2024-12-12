package org.task.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.task_management_system.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthorId(Long authorId);
    List<Task> findByAssigneeId(Long assigneeId);
}