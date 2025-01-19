package be.pxl.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAction(UnauthorizedActionException ex) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "Forbidden");
        responseBody.put("message", ex.getMessage()); // Include the exception message

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }

    // Handle other exceptions as needed
}
