package khaitq.infra.persitence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryDb extends JpaRepository<UserEntity, String> {


    @Query("SELECT u FROM UserEntity u WHERE u.id = :id")
    UserEntity findByIdAndTasks(@Param("id") long id);

    @EntityGraph(attributePaths = "tasks")
    @Query("SELECT u FROM UserEntity u")
    List<UserEntity> findAllWithTasksByGraph();

    Optional<UserEntity> findByEmail(String email);
}
