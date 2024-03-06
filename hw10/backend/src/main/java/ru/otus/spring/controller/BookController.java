package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/library/books")
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @PutMapping("/api/library/book")
    public BookUpdateDto edit(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto);
    }

    @PostMapping("/api/library/book")
    public BookCreateDto add(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return bookService.create(bookCreateDto);
    }

    @DeleteMapping("/api/library/book")
    public String delete(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "Book with id %d deleted".formatted(id);
    }


}
