package khaitq.infra;

import khaitq.domain.Task;
import khaitq.domain.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@lombok.AllArgsConstructor
public class TaskRepoImpl implements TaskRepository {

    private final TaskRepositoryDb repositoryDb;
    private final UserRepositoryDb userrepositoryDb;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Task> findAll() {
        return repositoryDb.findAll().stream()
                .map(e -> modelMapper.map(e, Task.class))
                .toList();
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = modelMapper.map(task, TaskEntity.class);
        UserEntity userEntity = UserEntity.builder().id(task.getUserId()).build();
        entity.setUser(userEntity);
        return modelMapper.map(repositoryDb.save(entity), Task.class);
    }

    @Override
    public Task findById(long id) {
        return repositoryDb.findById(id).map(e -> modelMapper.map(e, Task.class)).orElse(null);
    }
}
