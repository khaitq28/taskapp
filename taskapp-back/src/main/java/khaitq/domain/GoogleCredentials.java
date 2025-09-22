package khaitq.domain;

import khaitq.domain.user.UserId;

public final class GoogleCredentials implements Credentials {

    private final UserId userId;
    private final String googleSub;

    public GoogleCredentials(UserId userId, String googleSub) {
        this.userId = userId;
        this.googleSub = googleSub;
    }

    @Override
    public UserId userId() {
        return userId;
    }

    public String googleSub() {
        return googleSub;
    }
}
