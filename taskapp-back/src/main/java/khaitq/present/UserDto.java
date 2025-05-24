package khaitq.present;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDto extends BaseUserDto {
    private Long id;

}
