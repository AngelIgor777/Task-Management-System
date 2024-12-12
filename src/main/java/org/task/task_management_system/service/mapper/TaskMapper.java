package org.task.task_management_system.service.mapper;

import org.springframework.stereotype.Component;
import org.task.task_management_system.dto.response.TaskResponse;
import org.task.task_management_system.entity.Task;

import java.time.format.DateTimeFormatter;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setAuthorEmail(task.getAuthor().getEmail());
        response.setAssigneeEmail(task.getAssignee() != null ? task.getAssignee().getEmail() : null);
        response.setCreatedAt(task.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.setUpdatedAt(task.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
}