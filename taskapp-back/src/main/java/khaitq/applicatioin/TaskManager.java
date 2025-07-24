package khaitq.applicatioin;

import jakarta.annotation.PostConstruct;
import khaitq.domain.exception.EntityNotFoundException;
import khaitq.domain.task.Task;
import khaitq.domain.task.TaskRepository;
import khaitq.domain.user.UserRepository;
import khaitq.rest.dto.CreateUpdateTaskDto;
import khaitq.rest.dto.TaskDto;
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

    public List<TaskDto> getByUserId(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        return taskRepository.findByUserId(userId).stream()
                .map(e -> modelMapper.map(e, TaskDto.class))
                .toList();
    }

    public TaskDto createTask(CreateUpdateTaskDto dto, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Task task = modelMapper.map(dto, Task.class);
        task.setId(null);
        task.setUserId(userId);
        task = taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }

    public TaskDto updateTask(long id, CreateUpdateTaskDto dto) {
        Task task = taskRepository.findById(id);
        if (task == null) throw new EntityNotFoundException("Task",id);
        modelMapper.map(dto, task);
        task.setId(id);
        task = taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }

    @PostConstruct
    public void init() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public TaskDto getById(long id) throws EntityNotFoundException {
        Task task = taskRepository.findById(id);
        if (task == null) throw new EntityNotFoundException("Task ", id);
        return modelMapper.map(task, TaskDto.class);
    }

    public void deleteTask(long id) {
        Task task = taskRepository.findById(id);
        if (task == null) throw new EntityNotFoundException("Task ", id);
        taskRepository.delete(task);
    }
}
