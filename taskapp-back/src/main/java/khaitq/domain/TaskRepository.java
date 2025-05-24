package khaitq.domain;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task findById(long id);
}
