package hcmute.kltn.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(RuntimeException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

}
