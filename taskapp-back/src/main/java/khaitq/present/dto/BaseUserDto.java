package khaitq.present.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseUserDto {
    protected String name;
    protected String email;
    protected String role;
}
