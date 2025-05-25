package khaitq.present.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto extends CreateUpdateTaskDto {
    private Long id;
}
