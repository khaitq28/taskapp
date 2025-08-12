package khaitq.domain.user;


public record UserId (String value) {

    public UserId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or blank");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
