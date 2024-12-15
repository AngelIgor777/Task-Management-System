package org.task.task_management_system.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.task.task_management_system.entity.Role;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.repository.UserRepository;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskSecurityService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public boolean canChangeOrWatch(Long taskId, String token) {
        String userEmail = jwtService.getUsernameFromToken(token);
        boolean isAdmin = isRoleAdmin(userEmail);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        boolean isAssignee = task.getAssignee().getEmail().equals(userEmail);

        return isAssignee || isAdmin;
    }

    public boolean canChangeOrWatch(String token) {
        String userEmail = jwtService.getUsernameFromToken(token);
        return isRoleAdmin(userEmail);
    }


    private boolean isRoleAdmin(String userEmail) {
        Set<Role> roles = userRepository.findByEmail(userEmail).get().getRoles();

        return roles
                .stream().anyMatch(role -> role.getName().name().equals("ADMIN"));
    }
}
