package khaitq.applicatioin;


import khaitq.domain.Identity;
import khaitq.domain.user.User;
import khaitq.domain.user.UserId;
import khaitq.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class IdentityProvider {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;

    public Identity authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return Identity.builder()
                .userId(user.getUserId().getValue())
                .email(user.getEmail())
                .role(user.getRole())
                .name(user.getName())
                .build();
    }


    public Identity provisionFromGoogle(String email, Map<String, Object> attrs) {
        return Identity.fromUser(
                userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userId(UserId.builder().value(java.util.UUID.randomUUID().toString()).build())
                            .email(email)
                            .name((String) attrs.getOrDefault("name", "Unknown"))
                            .role("USER")
                            .build();
                    return userRepository.save(newUser);
                })
        );
    }

}
