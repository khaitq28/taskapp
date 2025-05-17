package khaitq.infra;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepositoryDb extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tasks")
    List<UserEntity> findAllWithTasks();

    @EntityGraph(attributePaths = "tasks")
    @Query("SELECT u FROM UserEntity u")
    List<UserEntity> findAllWithTasksByGraph();
}
