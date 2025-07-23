package khaitq.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateTaskDto {
    private String title;
    private String des;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}
