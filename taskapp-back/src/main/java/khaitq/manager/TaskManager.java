package khaitq.manager;

import jakarta.annotation.PostConstruct;
import khaitq.domain.Task;
import khaitq.domain.TaskRepository;
import khaitq.domain.UserRepository;
import khaitq.present.dto.CreateUpdateTaskDto;
import khaitq.present.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
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

    public TaskDto createTask(CreateUpdateTaskDto dto) {
        userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = modelMapper.map(dto, Task.class);
        task.setId(null);
        task = taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }

    public TaskDto updateTask(long id, CreateUpdateTaskDto dto) {
        Task task = taskRepository.findById(id);
        if (task == null) throw new RuntimeException("Task not found with id: " + id);
        modelMapper.map(dto, task);
        task.setId(id);
        task = taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }

    @PostConstruct
    public void init() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public TaskDto getById(long id) {
        Task task = taskRepository.findById(id);
        if (task == null) throw new RuntimeException("Task not found with id: " + id);
        return modelMapper.map(task, TaskDto.class);
    }

    public void deleteTask(long id) {
        Task task = taskRepository.findById(id);
        if (task == null) throw new RuntimeException("Task not found with id: " + id);
        taskRepository.delete(task);
    }
}
