package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.model.Book;
import ru.otus.spring.service.BookService;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("all")
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }

    @GetMapping("/editBook")
    public String getBookForEditing(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookMapper.mapEntityToDto(bookService.findById(id)));
        return "editBook";
    }

    @PostMapping("/editBook")
    public String editBook(@Valid @ModelAttribute("bookDto") BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/editBook?id=" + bookDto.getId();
        }
        bookService.update(bookMapper.mapDtoToEntity(bookDto));
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String formEmptyBook(Model model) {
        model.addAttribute("book", BookDto.builder().build());
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(@Valid @ModelAttribute("bookDto") BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/addBook?id=" + bookDto.getId();
        }
        Book book = bookMapper.mapDtoToEntity(bookDto);
        bookService.create(book);
        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
