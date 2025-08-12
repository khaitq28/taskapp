package khaitq.domain.task;

import khaitq.domain.user.UserId;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task findById(TaskId taskId);

    List<Task> findByUserId(UserId userId);

    Task save(Task task);

    void delete(TaskId taskId);
}
