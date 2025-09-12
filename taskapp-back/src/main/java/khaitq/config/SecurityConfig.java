package khaitq.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    @Value("${app.security.client-app-id:taskapp-frontend}")
    private String clientAppId;
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/swagger-ui.html", "/swagger-ui/**",
                "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml"
        );
    }
    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()   // <— mở swagger
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

//                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/tasks/**").hasRole("task_reader")
//                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/**").hasRole("task_reader")
//                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("task_reader")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(this::jwtAuthConverter)));
        return http.build();
    }

    private AbstractAuthenticationToken jwtAuthConverter(Jwt jwt) {
        JwtGrantedAuthoritiesConverter scopesConv = new JwtGrantedAuthoritiesConverter();
        scopesConv.setAuthorityPrefix("SCOPE_");
        Collection<GrantedAuthority> authorities = new ArrayList<>(scopesConv.convert(jwt));

        Map<String, Object> resAccess = jwt.getClaim("resource_access");

        if (resAccess != null && resAccess.get(clientAppId) instanceof Map<?,?> client) {
            Object cr = client.get("roles");
            if (cr instanceof Collection<?> croles) {
                croles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
            }
        }

        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

}
