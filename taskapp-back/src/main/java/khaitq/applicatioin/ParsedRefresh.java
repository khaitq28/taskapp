package khaitq.applicatioin;

import java.time.Instant;

public record ParsedRefresh(String email, String jti, Instant exp) {}

