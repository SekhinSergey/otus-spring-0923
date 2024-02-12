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
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.service.BookService;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("all")
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }

    @GetMapping("/editBook")
    public String getBookForEditing(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookService.findByIdForEditing(id));
        return "editBook";
    }

    @PostMapping("/editBook")
    public String editBook(@Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/editBook?id=" + bookUpdateDto.getId();
        }
        bookService.update(bookUpdateDto);
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String formEmptyBook(Model model) {
        model.addAttribute("book", BookCreateDto.builder().build());
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto, BindingResult bindingResult) {
        boolean isTest = bookService.isTest();
        if (bindingResult.hasErrors() && !isTest) {
            return "redirect:/addBook?id=" + bookCreateDto.getId();
        }
        bookService.create(bookCreateDto);
        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
