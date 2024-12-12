package org.task.task_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentRequest {
    private Long taskId;
    private String content;
}