package khaitq.config;

import khaitq.applicatioin.TaskManager;
import khaitq.applicatioin.UserManager;
import khaitq.rest.dto.BaseUserDto;
import khaitq.rest.dto.CreateUpdateTaskDto;
import khaitq.rest.dto.CreateUserDto;
import khaitq.rest.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserManager userManager;
    private final TaskManager taskManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        int taskCount = 1;
        if (userManager.countUsers() > 0) return;
        for (int i = 1; i < 5; i++) {
            CreateUserDto user = CreateUserDto.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .password(passwordEncoder.encode("password" + i))
                    .role(i == 1 || i == 2 ? "USER" : "ADMIN")
                    .build();
            UserDto createdUser = userManager.save(user);

            for (int j = 0; j < 5; j++) {
                String status = j % 2 == 0 ? "DOING" : "DONE";
                CreateUpdateTaskDto task = CreateUpdateTaskDto.builder()
                        .title("Task " + taskCount)
                        .des("Des for task " + taskCount)
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
