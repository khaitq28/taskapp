package khaitq.applicatioin;

import khaitq.domain.Identity;

public interface TokenService {

    String issueAccessToken(Identity id);      // chứa sub, email, roles
    String issueRefreshToken(Identity id);     // tuỳ bạn: có thể là JWT hoặc opaque lưu DB
    Identity verifyAccessToken(String jwt);
}
