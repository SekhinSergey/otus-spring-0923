package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbyid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find book by title", key = "bbyt")
    public String findBookByTitle(String title) {
        return bookService.findByTitle(title)
                .map(bookConverter::bookToString)
                .orElse("Book with title %s not found".formatted(title));
    }

    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, List<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, List<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }

    @ShellMethod(value = "Count book by author id", key = "cbbyaid")
    public int countByAuthorId(long authorId) {
        return bookService.countByAuthorId(authorId);
    }

    @ShellMethod(value = "Count book by author full name", key = "cbbyafn")
    public int countByAuthorFullName(String authorFullName) {
        return bookService.countByAuthorFullName(authorFullName);
    }

    @ShellMethod(value = "Count book by genre id", key = "cbbygid")
    public int countByGenreId(long genreId) {
        return bookService.countByGenreId(genreId);
    }

    @ShellMethod(value = "Count book by genre name", key = "cbbygn")
    public int countByGenreName(String genreName) {
        return bookService.countByGenreName(genreName);
    }
}
