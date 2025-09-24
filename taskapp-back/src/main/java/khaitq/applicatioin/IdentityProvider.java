package khaitq.applicatioin;


import khaitq.domain.Identity;
import khaitq.domain.user.User;
import khaitq.domain.user.UserId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IdentityProvider {

    private final Map<String, User> users = new HashMap<>();
    public IdentityProvider() {
        for (int i = 1; i < 5; i++) {
            User user = User.builder()
                    .userId(UserId.builder().value(String.valueOf(i)).build())
                    .email("user" + i + "@example.com")
                    .name("user" + i)
                    .passwordHash("password")
                    .role(i == 1 || i == 2 ? "USER" : "ADMIN")
                    .build();
            users.put(user.getEmail(), user);
        }
    }

    public Identity authenticate(String email, String password) {
        User user = users.get(email);
        return Identity.builder()
                .userId(user.getUserId().getValue())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
