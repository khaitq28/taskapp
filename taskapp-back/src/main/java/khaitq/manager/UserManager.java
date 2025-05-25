package khaitq.manager;

import khaitq.domain.Task;
import khaitq.domain.TaskRepository;
import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.present.BaseUserDto;
import khaitq.present.UserDto;
import khaitq.present.UserTaskDto;
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

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public UserTaskDto getUserTasksById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        List<Task> tasks = taskRepository.findByUserId(id);
        return UserTaskDto.builder()
                .user(modelMapper.map(user, UserDto.class))
                .tasks(tasks.stream().map(task -> modelMapper.map(task, khaitq.present.TaskDto.class)).toList())
                .build();
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
