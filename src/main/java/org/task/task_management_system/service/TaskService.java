package org.task.task_management_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.task_management_system.dto.request.TaskRequest;
import org.task.task_management_system.dto.response.TaskResponse;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.exception.TaskNotFoundException;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.repository.UserRepository;
import org.task.task_management_system.service.mapper.TaskMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    // Создание задачи
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setPriority(taskRequest.getPriority());
        task.setAssignee(userRepository.findById(taskRequest.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        taskRepository.save(task);
        return taskMapper.toResponse(task);
    }

    // Получение всех задач
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Получение задачи по ID
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponse(task);
    }

    // Обновление задачи
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setPriority(taskRequest.getPriority());
        task.setAssignee(userRepository.findById(taskRequest.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        taskRepository.save(task);
        return taskMapper.toResponse(task);
    }

    // Удаление задачи
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        taskRepository.delete(task);
    }
}
