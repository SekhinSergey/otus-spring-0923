package ru.otus.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.spring.dto.response.ErrorDto;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

// По поводу аннотации с кодом - у ResponseEntity нет билдера только с телом, и текущее решение лаконичнее
@RestControllerAdvice
public class LibraryExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        Set<ErrorDto.Error> errors = exception.getBindingResult().getAllErrors().stream()
                .map(error -> ErrorDto.Error.builder()
                    .field(((FieldError) error).getField())
                    .message(error.getDefaultMessage())
                    .build())
                .collect(toSet());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .errors(errors)
                        .build());
    }

    // Считаю, что добавление DTO сюда бессмысленно, так как мои сообщения достаточно информативны
    // А колхозить с парсингом кусков сообщений для того, чтобы вытянуть какую-то инфу об объектах, явно хуже
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Непонятно, как предполагается использовать DTO, если в сообщении нет никакой инфы об объекте или поле
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        // Ни логирование, ни вывод в консоль обычный в хендлерах не работают
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
