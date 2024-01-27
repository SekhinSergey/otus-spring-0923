package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.model.Book;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @SuppressWarnings("all")
    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            return "No books found";
        }
        return books.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find book by id", key = "bbyid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create book", key = "bins")
    public String createBook(Book book) {
        var savedBook = bookService.create(book);
        return bookConverter.bookToString(savedBook);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(Book book) {
        var savedBook = bookService.update(book);
        return bookConverter.bookToString(savedBook);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete book by id", key = "bdelbyid")
    public String deleteBookById(String id) {
        bookService.deleteById(id);
        return "Book with id %d deleted successfully".formatted(id);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count book by author id", key = "cbbyaid")
    public int countBooksByAuthorId(String authorId) {
        return bookService.countByAuthorId(authorId);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count book by genre id", key = "cbbygid")
    public int countBooksByGenreId(String genreId) {
        return bookService.countByGenreId(genreId);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create book batch", key = "bnewb")
    public String createBookBatch(Set<Book> books) {
        List<Book> savedBooks = bookService.createBatch(books);
        if (books.isEmpty()) {
            return "No books saved";
        }
        return savedBooks.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update book batch", key = "bupdb")
    public String updateBookBatch(Set<Book> books) {
        List<Book> savedBooks = bookService.updateBatch(books);
        if (books.isEmpty()) {
            return "No books saved";
        }
        return savedBooks.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete books by ids", key = "bdelbyids")
    public String deleteBooksByIds(Set<String> ids) {
        bookService.deleteAllByIds(ids);
        return "Books deleted successfully";
    }
}
