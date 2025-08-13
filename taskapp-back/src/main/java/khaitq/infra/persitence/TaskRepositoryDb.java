package khaitq.infra.persitence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryDb extends JpaRepository<TaskEntity, String> {

    List<TaskEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}

