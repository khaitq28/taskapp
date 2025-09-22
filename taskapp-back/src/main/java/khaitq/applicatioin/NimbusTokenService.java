package khaitq.applicatioin;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import khaitq.domain.Identity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

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
                .subject(id.userId())
                .claim("email", id.email())
                .claim("role", id.role())  // ["USER","ADMIN"]
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
    public String issueRefreshToken(Identity id) {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(Duration.ofDays(refreshDays))))
                .subject(id.userId())
                .claim("typ", "refresh")
                .build();

        var header = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(kid).type(JOSEObjectType.JWT).build();
        var signed = new SignedJWT(header, claims);
        try {
            signed.sign(new RSASSASigner(privateKey));
            return signed.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Identity verifyAccessToken(String jwt) {
        try {
            var parsed = SignedJWT.parse(jwt);
            var verifier = new RSASSAVerifier(publicKey);
            if (!parsed.verify(verifier)) throw new BadCredentialsException("Invalid signature");
            var claims = parsed.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date())) throw new BadCredentialsException("Expired");
            return new Identity(
                    claims.getSubject(),
                    (String) claims.getClaim("email"),
                    (String) claims.getClaim("role")
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token", e);
        }
    }
}
