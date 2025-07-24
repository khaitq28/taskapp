package khaitq.domain.task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task findById(long id);

    List<Task> findByUserId(long userId);

    Task save(Task task);

    void delete(Task task);
}
