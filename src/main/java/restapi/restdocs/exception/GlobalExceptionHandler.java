package restapi.restdocs.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Error occurred");
        errorResponse.put("code", "ERROR_CODE");
        errorResponse.put("errors", ex.getFieldErrors().stream().map(fieldError -> {
            Map<String, String> errorDetail = new HashMap<>();
            errorDetail.put("field", fieldError.getField());
            errorDetail.put("value", fieldError.getRejectedValue().toString());
            errorDetail.put("reason", fieldError.getDefaultMessage());
            return errorDetail;
        }).collect(Collectors.toList()));

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
