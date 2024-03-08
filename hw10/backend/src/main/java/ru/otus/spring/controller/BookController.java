package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.service.BookService;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/library/books")
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    // Надо добавить параметр, вместо валидации руками его проверять и сетить? Зачем выполнять столько лишних действий?
    @PutMapping("/api/library/book")
    public ResponseEntity<BookDto> edit(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.update(bookUpdateDto));
    }

    @PostMapping("/api/library/book")
    public ResponseEntity<BookDto> add(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(bookCreateDto));
    }

    @DeleteMapping("/api/library/book")
    public ResponseEntity<String> delete(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(EMPTY);
    }


}
