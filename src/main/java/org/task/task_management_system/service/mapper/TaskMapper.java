package org.task.task_management_system.service.mapper;

import org.springframework.stereotype.Component;
import org.task.task_management_system.dto.request.TaskRequest;
import org.task.task_management_system.dto.response.TaskResponse;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.entity.User;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .authorEmail(task.getAuthor() != null ? task.getAuthor().getEmail() : null)
                .assigneeEmail(task.getAssignee() != null ? task.getAssignee().getEmail() : null)
                .createdAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : null)
                .updatedAt(task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : null)
                .build();
    }

    public Task toEntity(TaskRequest request) {
        User author = new User();
        author.setId(request.getAuthorId());

        User assignee = new User();
        assignee.setId(request.getAssigneeId());

        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .author(author)
                .assignee(assignee)
                .build();
    }

    public void updateEntityFromRequest(TaskRequest request, Task task) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
    }
}
