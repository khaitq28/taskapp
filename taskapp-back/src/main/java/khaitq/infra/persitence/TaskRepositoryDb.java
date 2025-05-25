package khaitq.infra.persitence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface TaskRepositoryDb extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByUserId(long userId);
}

