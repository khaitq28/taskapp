package khaitq.applicatioin;

import khaitq.domain.Task;
import khaitq.domain.TaskRepository;
import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.rest.dto.BaseUserDto;
import khaitq.rest.dto.UserDto;
import khaitq.rest.dto.UserTaskDto;
import khaitq.rest.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserManager {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public UserDto save(BaseUserDto dto) {
        User user = modelMapper.map(dto, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public List<UserTaskDto> getUsersWithTasks() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            List<Task> tasks = taskRepository.findByUserId(user.getId());
            return UserTaskDto.builder()
                    .user(modelMapper.map(user, UserDto.class))
                    .tasks(tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList())
                    .build();
        }).toList();
    }


    public UserTaskDto getUserTasksById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        List<Task> tasks = taskRepository.findByUserId(id);
        return UserTaskDto.builder()
                .user(modelMapper.map(user, UserDto.class))
                .tasks(tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList())
                .build();
    }

    public void deleteUser(long id) {
        // delete all tasks associated with the user
        List<Task> tasks = taskRepository.findByUserId(id);
        for (Task task : tasks) {
            taskRepository.delete(task);
        }
        userRepository.deleteById(id);
    }
}
