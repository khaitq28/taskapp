package khaitq.infra.persitence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepositoryDb extends JpaRepository<TaskEntity, Long> {
}
