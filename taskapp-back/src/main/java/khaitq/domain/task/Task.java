package khaitq.domain.task;


import khaitq.domain.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private TaskId taskId;
    private String title;
    private String des;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private UserId userId;
}
