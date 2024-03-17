package ru.otus.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.spring.dto.response.ErrorDto;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestControllerAdvice
public class LibraryExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Set<ErrorDto.Error> errors = exception.getBindingResult().getAllErrors().stream()
                .map(error -> ErrorDto.Error.builder()
                    .field(((FieldError) error).getField())
                    .message(error.getDefaultMessage())
                    .build())
                .collect(toSet());
        return ErrorDto.builder()
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        return exception.getMessage();
    }
}
