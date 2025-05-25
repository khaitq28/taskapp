package khaitq.config;

import khaitq.manager.TaskManager;
import khaitq.manager.UserManager;
import khaitq.present.dto.BaseUserDto;
import khaitq.present.dto.CreateUpdateTaskDto;
import khaitq.present.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserManager userManager;
    private final TaskManager taskManager;

    @Override
    public void run(String... args) {
        int taskCount = 1;
        for (int i = 1; i < 11; i++) {
            BaseUserDto user = BaseUserDto.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .role(i % 2 == 0 ? "USER" : "ADMIN")
                    .build();
            UserDto createdUser = userManager.save(user);

            for (int j = 0; j < 10; j++) {
                String status = j % 2 == 0 ? "DOING" : "DONE";
                CreateUpdateTaskDto task = CreateUpdateTaskDto.builder()
                        .title("Task " + taskCount)
                        .des("Description for task " + taskCount)
                        .status(status)
                        .userId(createdUser.getId())
                        .build();
                taskCount++;
                if (status.equals("DONE")) {
                    task.setFinishedAt(LocalDateTime.now());
                }
                task.setCreatedAt(LocalDateTime.now());
                taskManager.createTask(task);
            }
        }
    }
}
