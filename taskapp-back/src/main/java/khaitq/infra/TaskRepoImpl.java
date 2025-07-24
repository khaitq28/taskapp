package khaitq.infra;

import khaitq.domain.task.Task;
import khaitq.domain.task.TaskRepository;
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
                .map(e -> modelMapper.map(e, Task.class))
                .toList();
    }

    @Override
    public Task findById(long id) {
        return repositoryDb.findById(id).map(e -> modelMapper.map(e, Task.class)).orElse(null);
    }

    @Override
    public List<Task> findByUserId(long userId) {
        return repositoryDb.findByUserId(userId).stream()
                .map(e -> modelMapper.map(e, Task.class))
                .toList();
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity  = modelMapper.map(task, TaskEntity.class);
        return modelMapper.map(
                repositoryDb.save(entity),
                Task.class);
    }

    @Override
    public void delete(Task task) {
        repositoryDb.deleteById(task.getId());
    }
}
