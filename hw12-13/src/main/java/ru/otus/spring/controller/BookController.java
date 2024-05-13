package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.security.component.AccessService;
import ru.otus.spring.service.BookService;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AccessService accessService;

    @GetMapping("/api/library/books")
    public List<BookDto> getAll() {
        accessService.checkAccess();
        return bookService.findAll();
    }

    @PutMapping("/api/library/book/{id}")
    public ResponseEntity<BookDto> edit(@PathVariable long id, @Valid @RequestBody BookUpdateDto dto) {
        accessService.checkAccess();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.update(id, dto));
    }

    @PostMapping("/api/library/book")
    public ResponseEntity<BookDto> add(@Valid @RequestBody BookCreateDto dto) {
        accessService.checkAccess();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(dto));
    }

    @DeleteMapping("/api/library/book")
    public ResponseEntity<String> delete(@RequestParam("id") long id) {
        accessService.checkAccess();
        bookService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(EMPTY);
    }


}
