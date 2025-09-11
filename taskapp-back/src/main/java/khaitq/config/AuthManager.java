package khaitq.config;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("authManager")
@RequiredArgsConstructor
public class AuthManager {

    private final CurrentUserUtil current;
    private static final String ADMIN_ROLE = "admin";

    public boolean isCurrentUser(String email) {
        return email != null && current.email().equalsIgnoreCase(email);
    }

    public boolean isAdmin() {
        return current.hasRole(ADMIN_ROLE);
    }

    public String currentUserEmail() {
        return current.email();
    }
}
