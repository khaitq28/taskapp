package khaitq.present.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long userId;
}
