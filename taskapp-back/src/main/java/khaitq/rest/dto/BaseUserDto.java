package khaitq.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseUserDto {
    protected String name;
    @Valid
    @Email(message = "invalid email format")
    protected String email;
    protected String role;
}
