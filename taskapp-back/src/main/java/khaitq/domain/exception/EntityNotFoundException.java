package khaitq.domain.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(String entityName, Object id) {
        super(entityName + " with ID " + id + " not found");
    }
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
