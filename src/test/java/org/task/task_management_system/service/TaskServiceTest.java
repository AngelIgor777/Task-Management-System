package org.task.task_management_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.task.task_management_system.dto.request.TaskRequest;
import org.task.task_management_system.dto.response.TaskResponse;
import org.task.task_management_system.entity.Task;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.exception.TaskNotFoundException;
import org.task.task_management_system.repository.TaskRepository;
import org.task.task_management_system.repository.UserRepository;
import org.task.task_management_system.service.mapper.TaskMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
@Import({TaskService.class, UserService.class, TaskMapper.class})
public class TaskServiceTest {

    private static final String NEW_TASK_STATUS = "NEW";
    private static final String IN_PROGRESS_STATUS = "IN_PROGRESS";
    private static final String HIGH_PRIORITY = "HIGH";
    private static final String LOW_PRIORITY = "LOW";

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    private User author;
    private User assignee;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        author = new User();
        author.setEmail("author@example.com");
        author.setPassword("password");
        userRepository.save(author);

        assignee = new User();
        assignee.setEmail("assignee@example.com");
        assignee.setPassword("password");
        userRepository.save(assignee);
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        TaskRequest request = createTaskRequest("New Task", NEW_TASK_STATUS, HIGH_PRIORITY);

        TaskResponse response = taskService.createTask(request);

        assertThat(response.getTitle()).isEqualTo("New Task");
        assertThat(response.getStatus()).isEqualTo(NEW_TASK_STATUS);
        assertThat(response.getPriority()).isEqualTo(HIGH_PRIORITY);
    }

    @Test
    void shouldReturnAllTasks() {
        createSampleTask("Task 1");
        createSampleTask("Task 2");

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(tasks.get(1).getTitle()).isEqualTo("Task 2");
    }

    @Test
    void shouldUpdateTaskStatusAndPriority() {
        Task task = createSampleTask("Task to Update");

        TaskResponse updatedTask = taskService.updateTaskStatusAndPriority(task.getId(), IN_PROGRESS_STATUS, LOW_PRIORITY);

        assertThat(updatedTask.getStatus()).isEqualTo(IN_PROGRESS_STATUS);
        assertThat(updatedTask.getPriority()).isEqualTo(LOW_PRIORITY);
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        Task task = createSampleTask("Task to Delete");

        taskService.deleteTask(task.getId());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    @Test
    void shouldGetTaskByIdSuccessfully() {
        Task task = createSampleTask("Sample Task");

        Task foundTask = taskService.getTaskById(task.getId());

        assertThat(foundTask.getTitle()).isEqualTo("Sample Task");
    }

    @Test
    void shouldThrowTaskNotFoundExceptionForInvalidTaskId() {
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");
    }

    @Test
    void shouldGetTasksByAssigneeEmailSuccessfully() {
        createSampleTask("Task 1", assignee);
        createSampleTask("Task 2", assignee);

        Pageable pageable = PageRequest.of(0, 10);
        Page<TaskResponse> tasksPage = taskService.getTasksByAssigneeName(assignee.getEmail(), pageable);

        assertThat(tasksPage.getContent()).hasSize(2);
        assertThat(tasksPage.getTotalElements()).isEqualTo(2);
    }

    private Task createSampleTask(String title) {
        return createSampleTask(title, assignee);
    }

    private Task createSampleTask(String title, User assignee) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Test description");
        task.setStatus(NEW_TASK_STATUS);
        task.setPriority(HIGH_PRIORITY);
        task.setAuthor(author);
        task.setAssignee(assignee);

        return taskRepository.save(task);
    }

    private TaskRequest createTaskRequest(String title, String status, String priority) {
        TaskRequest request = new TaskRequest();
        request.setTitle(title);
        request.setDescription("Test task description");
        request.setStatus(status);
        request.setPriority(priority);
        request.setAuthorId(author.getId());
        request.setAssigneeId(assignee.getId());

        return request;
    }
}
