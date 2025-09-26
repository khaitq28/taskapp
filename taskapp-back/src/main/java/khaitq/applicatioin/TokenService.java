package khaitq.applicatioin;

import khaitq.domain.Identity;

public interface TokenService {

    String issueAccessToken(Identity id);     // RS256, có email/roles/name…
    String issueRefreshToken(String email);  // chỉ cần email (+ jti, exp, typ=refresh)
    ParsedRefresh parseRefresh(String refresh);

}
