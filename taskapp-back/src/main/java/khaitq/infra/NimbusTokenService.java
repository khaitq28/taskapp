package khaitq.infra;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import khaitq.applicatioin.ParsedRefresh;
import khaitq.applicatioin.TokenService;
import khaitq.domain.Identity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NimbusTokenService implements TokenService {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    @Value("${app.jwt.key.kid}") private String kid;
    @Value("${app.jwt.issuer}") private String issuer;
    @Value("${app.jwt.access-token-minutes}") private long accessMinutes;
    @Value("${app.jwt.refresh-token-days}") private long refreshDays;


    @Override
    public String issueAccessToken(Identity id) {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(Duration.ofMinutes(accessMinutes))))
                .subject(id.email())
                .claim("email", id.email())
                .claim("role", id.role())
                .claim("name", id.name())
                .claim("id", id.userId())
                .build();

        var header = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(kid).type(JOSEObjectType.JWT).build();
        var signed = new SignedJWT(header, claims);
        try {
            var signer = new RSASSASigner(privateKey);
            signed.sign(signer);
            return signed.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("JWT signing failed", e);
        }
    }

    @Override
    public String issueRefreshToken(String email) {
        Instant now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(email)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(Duration.ofDays(refreshDays))))
                .jwtID(UUID.randomUUID().toString())
                .claim("typ", "refresh")
                .build();
        return sign(claims);
    }

    @Override
    public ParsedRefresh parseRefresh(String refresh) {
        try {
            var jwt = SignedJWT.parse(refresh);
            if (!jwt.verify(new RSASSAVerifier(publicKey))) throw new JOSEException("bad sig");
            var c = jwt.getJWTClaimsSet();
            if (!"refresh".equals(c.getStringClaim("typ"))) throw new JOSEException("wrong typ");
            if (c.getExpirationTime().before(new Date()))    throw new JOSEException("expired");
            return new ParsedRefresh(c.getSubject(), c.getJWTID(), c.getExpirationTime().toInstant());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }

    private String sign(JWTClaimsSet claims) {
        try {
            var header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT).keyID(kid).build();
            var jwt = new SignedJWT(header, claims);
            jwt.sign(new RSASSASigner(privateKey));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Sign JWT failed", e);
        }
    }
}
