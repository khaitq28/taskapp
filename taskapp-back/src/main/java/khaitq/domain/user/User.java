package khaitq.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UserId userId;
    private String name;
    private String email;
    private String role;
    private String passwordHash;
}
