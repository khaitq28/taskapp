package khaitq.rest;


import khaitq.domain.exception.AccessDeniedException;
import khaitq.domain.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(EntityNotFoundException.class)
     public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
         ErrorResponse response = new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
     }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse response = new ErrorResponse("ACCESS_DENIED", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    public record ErrorResponse(String code, String message) {}
}
