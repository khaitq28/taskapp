package khaitq.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khaitq.applicatioin.AuthSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;


@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.popup-origin}")
    private String popupOrigin;

    private final AuthSessionService authSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String refresh = authSession.issueRefreshForGoogleLogin(oAuth2User.getAttributes());


        ResponseCookie cookie = ResponseCookie.from("refresh", refresh)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.getWriter().write("""
          <!doctype html><html><body><script>
            window.opener.postMessage({
              type: "GOOGLE_LOGIN_SUCCESS",    refreshToken: "%s"
            }, "%s");
            window.close();
          </script></body></html>
          """.formatted(refresh, popupOrigin));

    }

}
