package khaitq.applicatioin;

import khaitq.domain.Identity;
import khaitq.domain.user.User;
import khaitq.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthSessionService {

    private final UserRepository userRepository;
    private final IdentityProvider identityProvider;
    private final TokenService tokenService;

    public String issueRefreshForGoogleLogin(Map<String, Object> googleAttrs) {
        String email = (String) googleAttrs.get("email");
        Identity identity = identityProvider.provisionFromGoogle(email, googleAttrs);
        return tokenService.issueRefreshToken(identity.email());
    }

    public String issueAccessFromRefresh(String refreshCookie) {
        ParsedRefresh pr = tokenService.parseRefresh(refreshCookie);
        User user = userRepository.findByEmail(pr.email())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        Identity id = Identity.fromUser(user);
        return tokenService.issueAccessToken(id);
    }


    public LoginResult loginWithPassword(String email, String rawPassword) {
        Identity id = identityProvider.authenticate(email, rawPassword);
        String refresh = tokenService.issueRefreshToken(id.userId());
        String access  = tokenService.issueAccessToken(id);
        return new LoginResult(access, refresh);
    }
}
