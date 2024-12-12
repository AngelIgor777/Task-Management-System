package org.task.task_management_system;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.task.task_management_system.util.KeyUtil;

@SpringBootApplication
public class TaskManagementSystemApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        KeyUtil.setProperties(dotenv);

        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }

}
