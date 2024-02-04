package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.model.Book;
import ru.otus.spring.service.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String BOOKS = "books";

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute(BOOKS, bookService.findAll());
        return BOOKS;
    }

    @GetMapping("/editBook")
    public String getBookForEditing(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookMapper.mapEntityToDto(bookService.findById(id)));
        return "editBook";
    }

    @PostMapping("/editBook")
    public String editBook(BookDto bookDto) {
        bookService.update(bookMapper.mapDtoToEntity(bookDto));
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String formEmptyBook(Model model) {
        model.addAttribute("book", BookDto.builder().build());
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(BookDto bookDto) {
        Book book = bookMapper.mapDtoToEntity(bookDto);
        book.setId(null);
        bookService.create(book);
        return "redirect:/";
    }
}
