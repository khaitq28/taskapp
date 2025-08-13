package khaitq.infra;

import khaitq.domain.task.Task;
import khaitq.domain.task.TaskId;
import khaitq.domain.task.TaskRepository;
import khaitq.domain.user.UserId;
import khaitq.infra.persitence.TaskEntity;
import khaitq.infra.persitence.TaskRepositoryDb;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@lombok.AllArgsConstructor
public class TaskRepoImpl implements TaskRepository {

    private final TaskRepositoryDb repositoryDb;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Task> findAll() {
        return repositoryDb.findAll().stream()
                .map(this::entityToTask)
                .toList();
    }

    @Override
    public Task findById(TaskId taskId) {
        return repositoryDb.findById(taskId.getValue()).map(this::entityToTask).orElse(null);
    }

    private Task entityToTask(TaskEntity e) {
        Task task = modelMapper.map(e, Task.class);
        task.setTaskId(new TaskId(e.getId()));
        task.setUserId(new UserId(e.getUserId()));
        return task;
    }

    @Override
    public List<Task> findByUserId(UserId userId) {
        return repositoryDb.findByUserId(userId.getValue()).stream()
                .map(this::entityToTask)
                .toList();
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(task.getTitle());
        entity.setDes(task.getDes());
        entity.setStatus(task.getStatus());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setFinishedAt(task.getFinishedAt());
        entity.setId(task.getTaskId().getValue());
        entity.setUserId(task.getUserId().getValue());

        repositoryDb.save(entity);
        return task;
    }

    @Override
    public void delete(TaskId taskId) {
        repositoryDb.deleteById(taskId.getValue());
    }

    @Override
    public void deleteByUserId(UserId userId) {
        repositoryDb.deleteByUserId(userId.getValue());
    }
}
