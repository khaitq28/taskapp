package khaitq.manager;

import khaitq.domain.Task;
import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.present.BaseUserDto;
import khaitq.present.TaskDto;
import khaitq.present.UserDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public UserDto save(BaseUserDto dto) {
        User user = modelMapper.map(dto, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public long count() {
        return userRepository.count();
    }


    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User addTaskToUser(long userId, TaskDto dto) {
        User user = getById(userId);
        Task task = modelMapper.map(dto, Task.class);
        user.addTask(task);
        user = userRepository.save(user);
        return user;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
