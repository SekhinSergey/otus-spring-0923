package ru.otus.spring.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class LibraryExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(
                exception.getBindingResult().getAllErrors().stream()
                        .collect(Collectors.toMap(error ->
                                ((FieldError) error).getField(),
                                DefaultMessageSourceResolvable::getDefaultMessage,
                                (a, b) -> b)
                        ),
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }
}
