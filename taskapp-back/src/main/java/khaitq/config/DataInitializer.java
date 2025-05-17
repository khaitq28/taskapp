package khaitq.config;

import khaitq.domain.User;
import khaitq.present.TaskDto;
import khaitq.present.TaskManager;
import khaitq.present.UserManager;
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
            User user = User.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .role(i % 2 == 0 ? "USER" : "ADMIN")
                    .build();
            user = userManager.save(user);

            for (int j = 0; j < 4; j++) {
                String status = j % 2 == 0 ? "DOING" : "DONE";
                TaskDto task = TaskDto.builder()
                        .userId(user.getId())
                        .title("Task " + j)
                        .des("Description for task " + j)
                        .status(status)
                        .build();
                if (status.equals("DONE")) {
                    task.setFinishedAt(LocalDateTime.now());
                }
                taskManager.save(task);
            }
        }
    }
}
