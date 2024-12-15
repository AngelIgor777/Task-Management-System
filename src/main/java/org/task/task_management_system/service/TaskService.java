package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.task_management_system.dto.request.TaskRequest;
import org.task.task_management_system.dto.response.TaskResponse;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.exception.TaskNotFoundException;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.service.mapper.TaskMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;


    public TaskResponse createTask(TaskRequest taskRequest) {
        User author = userService.getUserById(taskRequest.getAuthorId());
        User assignee = userService.getUserById(taskRequest.getAssigneeId());

        Task task = taskMapper.toEntity(taskRequest);
        task.setAuthor(author);
        task.setAssignee(assignee);

        taskRepository.save(task);
        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Page<TaskResponse> getTasks(Pageable pageable, String status, String priority, Long authorId, Long assigneeId) {
        return taskRepository.findAllByFilters(pageable, status, priority, authorId, assigneeId)
                .map(taskMapper::toResponse);
    }

    public TaskResponse updateTaskStatusAndPriority(Long taskId, String status, String priority) {
        Task task = getTaskById(taskId);

        task.setStatus(status);
        task.setPriority(priority);

        Task updatedTask = taskRepository.save(task);

        return taskMapper.toResponse(updatedTask);
    }

    public Page<TaskResponse> getTasksByAssigneeName(String assigneeEmail, Pageable pageable) {
        User assignee = userService.getUserByEmail(assigneeEmail);

        Page<Task> tasks = taskRepository.findByAssignee(assignee, pageable);

        return tasks.map(taskMapper::toResponse);
    }

    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = getTaskById(taskId);

        taskMapper.updateEntityFromRequest(taskRequest, task);

        task.setAssignee(userService.getUserById(taskRequest.getAssigneeId()));

        taskRepository.save(task);
        return taskMapper.toResponse(task);
    }

    public void deleteTask(Long taskId) {
        Task task = getTaskById(taskId);
        taskRepository.delete(task);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}
