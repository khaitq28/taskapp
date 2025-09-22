package khaitq.rest;


import jakarta.validation.Valid;
import khaitq.applicatioin.IdentityProvider;
import khaitq.applicatioin.TokenService;
import khaitq.rest.dto.LoginRequest;
import khaitq.rest.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {


    private final IdentityProvider passwordProvider;   // Cách A: provider cho local
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        var id = passwordProvider.authenticate(req.email(), req.password()); // throw nếu sai
        var access = tokenService.issueAccessToken(id);
        var refresh = tokenService.issueRefreshToken(id);
        return ResponseEntity.ok(new TokenResponse(access, refresh));
        // hoặc set refresh vào HttpOnly cookie:
        // ResponseCookie cookie = ResponseCookie.from("refresh", refresh).httpOnly(true)...
    }

}
