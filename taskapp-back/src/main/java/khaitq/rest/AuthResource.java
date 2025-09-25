package khaitq.rest;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final IdentityProvider passwordProvider;
    private final TokenService tokenService;
    private final Map<String, Map<String, String>> oauth2Flows = new ConcurrentHashMap<>();


    @PostMapping("/refresh")
    public ResponseEntity<Map<String,String>> refresh(@CookieValue(name="refresh", required=false) String refreshCookie) {
        if (refreshCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","missing_refresh"));
        }
        Identity id = tokenService.verifyAccessToken(refreshCookie);
        String access = tokenService.issueAccessToken(id);
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        var id = passwordProvider.authenticate(req.email(), req.password());
        var access = tokenService.issueAccessToken(id);
        var refresh = tokenService.issueRefreshToken(id);
        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }

    @GetMapping("/google-login")
    public ResponseEntity<String> googleLogin() {
        Map<String, String> flowData = createOAuth2FlowData();
        String googleAuthorizeUrl = buildGoogleAuthorizeUrl(flowData);
        return ResponseEntity.status(302).header("Location", googleAuthorizeUrl).build();
    }

    @GetMapping("/google-callback")
    public ResponseEntity<String> googleCallback(@RequestParam String code, @RequestParam String state) {
        try {
            Map<String, String> flow = validateState(state);
            Map<String, Object> tokenResponse = exchangeCodeForTokens(code, flow);
            GoogleIdToken.Payload payload = verifyIdToken((String) tokenResponse.get("id_token"), flow);

            var user = passwordProvider.authenticate(payload.getEmail(), null);
            var appAccessToken = tokenService.issueAccessToken(user);
            var appRefreshToken = tokenService.issueRefreshToken(user);

            return buildHtmlResponse();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    private Map<String, String> createOAuth2FlowData() {
        String flowId = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();
        String codeVerifier = UUID.randomUUID().toString();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        Map<String, String> data = Map.of(
                "state", state,
                "nonce", nonce,
                "code_verifier", codeVerifier,
                "redirect_uri_expected", "http://localhost:8080/auth/callback",
                "time", String.valueOf(System.currentTimeMillis())
        );

        oauth2Flows.put(flowId, data);
        return data;
    }

    private String buildGoogleAuthorizeUrl(Map<String, String> flowData) {
        return UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", "430743706570-rh1rc6gppdshtjbnl136vhgcfu5tvfrk.apps.googleusercontent.com")
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .queryParam("redirect_uri", "http://localhost:8080/auth/google-callback")
                .queryParam("state", flowData.get("state"))
                .queryParam("nonce", flowData.get("nonce"))
                .queryParam("code_challenge", generateCodeChallenge(flowData.get("code_verifier")))
                .queryParam("code_challenge_method", "S256")
                .build()
                .toUriString();
    }

    private Map<String, String> validateState(String state) {
        return oauth2Flows.values().stream()
                .filter(f -> f.get("state").equals(state))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid state"));
    }

    private Map<String, Object> exchangeCodeForTokens(String code, Map<String, String> flow) {
        String tokenEndpoint = "https://oauth2.googleapis.com/token";
        Map<String, String> tokenRequest = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", flow.get("redirect_uri_expected"),
                "client_id", "430743706570-rh1rc6gppdshtjbnl136vhgcfu5tvfrk.apps.googleusercontent.com",
                "client_secret", "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",
                "code_verifier", flow.get("code_verifier")
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(convertToMultiValueMap(tokenRequest), headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Error exchanging code for token");
        }

        return response.getBody();
    }

    private GoogleIdToken.Payload verifyIdToken(String idToken, Map<String, String> flow) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("430743706570-rh1rc6gppdshtjbnl136vhgcfu5tvfrk.apps.googleusercontent.com"))
                .setIssuer("https://accounts.google.com")
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (googleIdToken == null) {
            throw new IllegalArgumentException("Invalid ID token");
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        if (!flow.get("nonce").equals(payload.get("nonce"))) {
            throw new IllegalArgumentException("Invalid nonce");
        }

        return payload;
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error generating code challenge", e);
        }
    }

    private MultiValueMap<String, String> convertToMultiValueMap(Map<String, String> map) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        return multiValueMap;
    }

    private ResponseEntity<String> buildHtmlResponse() {
        String htmlResponse = """
                <html>
                <body>
                    <script>
                        window.opener.postMessage("GOOGLE_LOGIN_SUCCESS", "*");
                        window.close();
                    </script>
                </body>
                </html>
                """;
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlResponse);
    }
}