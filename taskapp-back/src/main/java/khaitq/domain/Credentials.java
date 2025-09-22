package khaitq.domain;

import khaitq.domain.user.UserId;

public sealed interface Credentials permits LocalCredentials, GoogleCredentials {

    UserId userId();
}