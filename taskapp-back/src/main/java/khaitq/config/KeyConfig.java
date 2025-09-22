package khaitq.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class KeyConfig {

    private final Environment env;

    @Bean
    public RSAPublicKey rsaPublicKey() throws Exception {
        String pemContent = readPemContent("app.jwt.key.public");
        byte[] keyBytes = extractPemKeyContent(pemContent, "PUBLIC");

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() throws Exception {
        String pemContent = readPemContent("app.jwt.key.private");
        byte[] keyBytes = extractPemKeyContent(pemContent, "PRIVATE");

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    @Bean
    public JWK jwk(RSAPublicKey pub, RSAPrivateKey prv, @Value("${app.jwt.key.kid}") String kid) {
        return new RSAKey.Builder(pub).privateKey(prv).keyID(kid).build();
    }

    @Bean
    public JWKSet jwkSet(JWK jwk) {
        return new JWKSet(jwk);
    }


    private String readPemContent(String propertyKey) throws Exception {
        var url = ResourceUtils.getURL(env.getProperty(propertyKey));
        return new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private byte[] extractPemKeyContent(String pemContent, String keyType) {
        String cleaned = pemContent
                .replace("-----BEGIN " + keyType + " KEY-----", "")
                .replace("-----END " + keyType + " KEY-----", "")
                .replaceAll("\\s", ""); // Remove all whitespace (newlines, spaces, etc.)

        return Base64.getDecoder().decode(cleaned);
    }
}
