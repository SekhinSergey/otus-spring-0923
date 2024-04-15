package ru.otus.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.response.ErrorDto;

import static java.util.stream.Collectors.toSet;

@RestControllerAdvice
public class LibraryExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorDto> handleException(WebExchangeBindException exception) {
        return Mono.just(ErrorDto.builder()
                .errors(exception.getBindingResult().getAllErrors().stream()
                        .map(error -> ErrorDto.Error.builder()
                                .field(((FieldError) error).getField())
                                .message(error.getDefaultMessage())
                                .build())
                        .collect(toSet()))
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Mono<String> handleNotFoundException(NotFoundException exception) {
        return Mono.just(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Mono<String> handleException(Exception exception) {
        return Mono.just(exception.getMessage());
    }
}
