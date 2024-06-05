package hcmute.kltn.backend.exception;

import hcmute.kltn.backend.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.badRequest().body("ERROR:  " + ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleMultipartException(MissingServletRequestPartException ex) {
        ex.printStackTrace();
        return ResponseEntity.badRequest().body("ERROR:  " + ex.getMessage());
    }
}
