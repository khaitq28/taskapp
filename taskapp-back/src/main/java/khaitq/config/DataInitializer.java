package khaitq.config;

import khaitq.domain.Task;
import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.manager.TaskManager;
import khaitq.manager.UserManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .role(i % 2 == 0 ? "USER" : "ADMIN")
                    .build();

            for (int j = 0; j < 4; j++) {
                String status = j % 2 == 0 ? "DOING" : "DONE";
                Task task = Task.builder()
                        .title("Task " + j)
                        .des("Description for task " + j)
                        .status(status)
                        .build();
                if (status.equals("DONE")) {
                    task.setFinishedAt(LocalDateTime.now());
                }
                user.addTask(task);
            }
            userRepository.save(user);
        }
    }
}
