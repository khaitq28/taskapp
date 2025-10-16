package khaitq.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khaitq.applicatioin.AuthSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthResource {


    private final AuthSessionService authSession;

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,String>> refresh(@CookieValue(name="refresh", required=false) String refreshCookie, HttpServletRequest req) {

        log.info("[REFRESH] Origin={}, Referer={}, Cookie={}",
                req.getHeader("Origin"), req.getHeader("Referer"), req.getHeader("Cookie"));

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