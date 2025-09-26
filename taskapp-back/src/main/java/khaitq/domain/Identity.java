package khaitq.domain;


import khaitq.domain.user.User;
import lombok.Builder;

@Builder
public record Identity(String userId, String name, String email, String role) {
    public static Identity fromUser(User u) {
        return new Identity(u.getUserId().value(), u.getName(), u.getEmail(), u.getRole());
    }
}
