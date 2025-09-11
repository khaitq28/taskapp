package khaitq.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;


@Component
public class CurrentUserUtil {

    public JwtAuthenticationToken auth() {
        var a = SecurityContextHolder.getContext().getAuthentication();
        if (a instanceof JwtAuthenticationToken t && a.isAuthenticated()) return t;
        throw new IllegalStateException("No JWT authentication");
    }

    public Jwt jwt() { return auth().getToken(); }

    public String id() { return jwt().getSubject(); }

    public String email() {
        var j = jwt();
        var e = j.getClaimAsString("email");
        if (e != null) return e;
        var u = j.getClaimAsString("preferred_username");
        return (u != null) ? u : j.getSubject();
    }

    public boolean hasRole(String role) {
        return auth().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
