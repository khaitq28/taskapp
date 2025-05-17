package khaitq.domain;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task save(Task task);

    Task findById(long id);
}
