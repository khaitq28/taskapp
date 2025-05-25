package khaitq.config;

import khaitq.domain.Task;
import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.manager.TaskManager;
import khaitq.manager.UserManager;
import khaitq.present.BaseUserDto;
import khaitq.present.TaskDto;
import khaitq.present.UserDto;
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
        for (int i = 0; i < 10; i++) {
            BaseUserDto user = BaseUserDto.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .role(i % 2 == 0 ? "USER" : "ADMIN")
                    .build();
            UserDto createdUser = userManager.save(user);

            for (int j = 0; j < 4; j++) {
                String status = j % 2 == 0 ? "DOING" : "DONE";
                TaskDto task = TaskDto.builder()
                        .title("Task " + j)
                        .des("Description for task " + j)
                        .status(status)
                        .userId(createdUser.getId())
                        .build();
                if (status.equals("DONE")) {
                    task.setFinishedAt(LocalDateTime.now());
                }
                task.setCreatedAt(LocalDateTime.now());
                taskManager.saveTask(task);
            }
        }
    }
}
