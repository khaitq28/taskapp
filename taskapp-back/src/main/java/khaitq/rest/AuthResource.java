package khaitq.rest;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import khaitq.applicatioin.AuthSessionService;
import khaitq.applicatioin.IdentityProvider;
import khaitq.applicatioin.TokenService;
import khaitq.domain.Identity;
import khaitq.rest.dto.LoginRequest;
import khaitq.rest.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {


    private final AuthSessionService authSession;

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,String>> refresh(@CookieValue(name="refresh", required=false) String refreshCookie) {
        if (refreshCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","missing_refresh"));
        }
        String access = authSession.issueAccessFromRefresh(refreshCookie);
        return ResponseEntity.ok(Map.of("access", access));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res) {
        ResponseCookie expired = ResponseCookie.from("refresh","")
                .httpOnly(true).secure(true).sameSite("Lax").path("/auth/refresh")
                .maxAge(0).build();
        res.setHeader(HttpHeaders.SET_COOKIE, expired.toString());
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> body,
                                                    HttpServletResponse res) {
        String email = body.get("email");
        String password = body.get("password");
        var result = authSession.loginWithPassword(email, password);
        ResponseCookie cookie = ResponseCookie.from("refresh", result.refresh())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(14))
                .build();
        res.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("access", result.access()));
    }




}