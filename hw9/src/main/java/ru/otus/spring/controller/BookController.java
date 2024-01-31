package ru.otus.spring.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.model.Book;
import ru.otus.spring.service.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String BOOKS = "books";

    private final BookService bookService;

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute(BOOKS, bookService.findAll());
        return BOOKS;
    }

    @GetMapping("/editBook")
    public String getBookForEditing(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        return "editBook";
    }

    @PostMapping("/editBook")
    public String edit(Book book) {
        bookService.update(book);
        return "redirect:/";
    }
}
