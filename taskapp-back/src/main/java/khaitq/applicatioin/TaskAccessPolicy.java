package khaitq.applicatioin;

import khaitq.domain.exception.EntityNotFoundException;
import khaitq.domain.task.Task;
import khaitq.domain.task.TaskId;
import khaitq.domain.task.TaskRepository;
import khaitq.domain.user.User;
import khaitq.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Quang-Khai TRAN
 * @date 12/09/2025
 */


@Service
@AllArgsConstructor
public class TaskAccessPolicy {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public boolean canAccessTask(String taskId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User", userEmail));
        return taskRepository.findByUserId(user.getUserId()).stream().anyMatch(t -> t.getTaskId().getValue().equals(taskId));
    }


}
