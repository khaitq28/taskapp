package khaitq.present;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private long userId;
    private String title;
    private String des;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}
