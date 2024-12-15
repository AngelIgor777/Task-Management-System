package org.task.task_management_system.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class   CommentResponse {
    private Long id;
    private Long taskId;
    private String taskTitle;
    private String userEmail;
    private String content;
    private String createdAt;
}