package khaitq.rest.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDto extends BaseUserDto {
    private String id;

}
