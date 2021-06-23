package authapi.global;

import authapi.user.domain.exception.InvalidEmail;
import authapi.user.domain.exception.InvalidPassword;
import authapi.global.response.ArgumentErrorResponse;
import authapi.global.response.ErrorResponse;
import authapi.user.usecase.exception.EmailAlreadyRegistered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EmailAlreadyRegistered.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegistered(EmailAlreadyRegistered exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidEmail.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmail(InvalidEmail exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<ErrorResponse> handleInvalidPassword(InvalidPassword exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ArgumentErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ArgumentErrorResponse> errors = exception.getFieldErrors()
                .stream()
                .map(error -> new ArgumentErrorResponse(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
