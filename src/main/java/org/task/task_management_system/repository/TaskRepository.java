package org.task.task_management_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.entity.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAssignee(User assignee, Pageable pageable);

    @Query("SELECT t FROM Task t " +
            "WHERE (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:authorId IS NULL OR t.author.id = :authorId) " +
            "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    Page<Task> findAllByFilters(Pageable pageable,
                                @Param("status") String status,
                                @Param("priority") String priority,
                                @Param("authorId") Long authorId,
                                @Param("assigneeId") Long assigneeId);
}