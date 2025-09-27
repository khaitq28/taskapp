package khaitq.applicatioin;

import jakarta.annotation.PostConstruct;
import khaitq.domain.exception.AccessDeniedException;
import khaitq.domain.exception.EntityNotFoundException;
import khaitq.domain.task.Task;
import khaitq.domain.task.TaskId;
import khaitq.domain.task.TaskRepository;
import khaitq.domain.user.User;
import khaitq.domain.user.UserId;
import khaitq.domain.user.UserRepository;
import khaitq.rest.dto.CreateUpdateTaskDto;
import khaitq.rest.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class TaskManager {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    private final AuthzManager authzManager;
    private final TaskAccessPolicy taskAccessPolicy;

    public List<TaskDto> getByUserId(String userId) {
        UserId id = new UserId(userId);
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", userId));
        return taskRepository.findByUserId(id).stream()
                .map(e -> modelMapper.map(e, TaskDto.class))
                .toList();
    }

    public List<TaskDto> getAllTasks() {
        if (authzManager.isAdmin())
            return taskRepository.findAll().stream()
                    .map(e -> modelMapper.map(e, TaskDto.class))
                    .toList();

        User user = userRepository.findByEmail(authzManager.currentUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User", authzManager.currentUserEmail()));
        return taskRepository.findByUserId(user.getUserId()).stream()
                .map(e -> modelMapper.map(e, TaskDto.class))
                .toList();
    }

    @Transactional
    public TaskDto createTask(CreateUpdateTaskDto dto) {
        userRepository.findById(new UserId(dto.getUserId())).orElseThrow(() -> new EntityNotFoundException("User", dto.getUserId()));
        Task task = modelMapper.map(dto, Task.class);
        task.setUserId(new UserId(dto.getUserId()));
        task.setTaskId(new TaskId(UUID.randomUUID().toString()));
        task.setCreatedAt(LocalDateTime.now());
        task = taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }


    @Transactional
    public TaskDto updateTask(String id, CreateUpdateTaskDto dto) {
        checkCurrentUserCanAccessTask(id);
        TaskId taskId = new TaskId(id);
        Task task = taskRepository.findById(taskId);
        if (task == null) throw new EntityNotFoundException("Task",id);
        modelMapper.map(dto, task);
        if (dto.getUserId() != null) {
            userRepository.findById(new UserId(dto.getUserId())).orElseThrow(() -> new EntityNotFoundException("User", dto.getUserId()));
            task.setUserId(new UserId(dto.getUserId()));
        }
        taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }

    @PostConstruct
    public void init() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public TaskDto getById(String id) throws EntityNotFoundException {
        checkCurrentUserCanAccessTask(id);
        TaskId taskId = new TaskId(id);
        Task task = taskRepository.findById(taskId);
        if (task == null) throw new EntityNotFoundException("Task ", id);
        return modelMapper.map(task, TaskDto.class);
    }

    @Transactional
    public void deleteTask(String id) {
        checkCurrentUserCanAccessTask(id);
        TaskId taskId = new TaskId(id);
        Task task = taskRepository.findById(taskId);
        if (task == null) throw new EntityNotFoundException("Task ", id);
        taskRepository.delete(taskId);
    }

    private void checkCurrentUserCanAccessTask(String taskId) {
        if (authzManager.isAdmin() || taskAccessPolicy.canAccessTask(taskId, authzManager.currentUserEmail()))
            return;
        throw new AccessDeniedException("You do not have permission to access this task.");
    }
}
