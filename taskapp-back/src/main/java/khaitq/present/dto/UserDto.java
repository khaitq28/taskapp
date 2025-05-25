package khaitq.present.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDto extends BaseUserDto {
    private Long id;

}
