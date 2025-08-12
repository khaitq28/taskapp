package khaitq.infra.persitence;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String title;
    private String des;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    @Column(nullable = false)
    private String userId;

//    @PrePersist
//    public void prePersist() {
//        if (createdAt == null) {
//            createdAt = LocalDateTime.now();
//        }
//        if (status != null && status.equalsIgnoreCase("DONE") && finishedAt == null) {
//            finishedAt = LocalDateTime.now();
//        }
//    }
}
