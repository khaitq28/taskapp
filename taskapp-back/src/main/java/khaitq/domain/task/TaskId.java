package khaitq.domain.task;

public record TaskId(String value) {

    public TaskId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TaskId cannot be null or blank");
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
