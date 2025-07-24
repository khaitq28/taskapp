package khaitq.domain.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String entityName, Object id) {
        super("Access denied for " + entityName + " with ID " + id);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
