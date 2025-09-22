package khaitq.domain;

import khaitq.domain.user.UserId;

public final class LocalCredentials implements Credentials {

    private final UserId userId;

    public LocalCredentials(UserId userId) {
        this.userId = userId;
    }

    @Override
    public UserId userId() {
        return userId;
    }
}
