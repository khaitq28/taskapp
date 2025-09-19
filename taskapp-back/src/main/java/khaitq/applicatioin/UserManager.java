package khaitq.applicatioin;

import khaitq.domain.exception.EntityNotFoundException;
import khaitq.domain.task.Task;
import khaitq.domain.task.TaskRepository;
import khaitq.domain.user.User;
import khaitq.domain.user.UserId;
import khaitq.domain.user.UserRepository;
import khaitq.rest.dto.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserManager {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public UserDto save(CreateUserDto dto) {
        User user = modelMapper.map(dto, User.class);
        user.setUserId(new UserId(UUID.randomUUID().toString()));
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public List<UserTaskDto> getUsersWithTasks() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            List<Task> tasks = taskRepository.findByUserId(user.getUserId());
            return UserTaskDto.builder()
                    .user(modelMapper.map(user, UserDto.class))
                    .tasks(tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList())
                    .build();
        }).toList();
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDto.setId(user.getUserId().getValue());
            return userDto;
        }).toList();
    }


    public UserTaskDto getUserWithTasksById(String id) throws EntityNotFoundException {
        User user = userRepository.findById(new UserId(id))
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        List<Task> tasks = taskRepository.findByUserId(new UserId(id));
        return UserTaskDto.builder()
                .user(modelMapper.map(user, UserDto.class))
                .tasks(tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList())
                .build();
    }


    @Transactional
    public void deleteUser(String userId) {
        UserId id = new UserId(userId);
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", userId));
        taskRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public UserTaskDto getUserWithTasksByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", email));
        List<Task> tasks = taskRepository.findByUserId(user.getUserId());
        return UserTaskDto.builder()
                .user(modelMapper.map(user, UserDto.class))
                .tasks(tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList())
                .build();
    }
}
