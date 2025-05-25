package khaitq.manager;

import khaitq.domain.Task;
import khaitq.domain.TaskRepository;
import khaitq.domain.User;
import khaitq.domain.UserRepository;
import khaitq.present.TaskDto;
import khaitq.present.UserResource;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class TaskManager {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(e -> modelMapper.map(e, TaskDto.class))
                .toList();
    }

    public TaskDto saveTask(TaskDto dto) {
        userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = modelMapper.map(dto, Task.class);
        task = taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }

}
