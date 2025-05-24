package khaitq.infra.persitence;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String des;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserEntity user;
}
