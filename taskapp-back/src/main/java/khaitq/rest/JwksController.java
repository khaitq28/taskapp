package khaitq.rest;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class JwksController {

    private final JWKSet jwkSet;

    @GetMapping("/jwks.json")
    public Map<String, Object> jwks() {
        return jwkSet.toPublicJWKSet().toJSONObject();
    }
}
