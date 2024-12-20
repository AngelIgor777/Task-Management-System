package org.task.task_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long authorId;
    private Long assigneeId;
}