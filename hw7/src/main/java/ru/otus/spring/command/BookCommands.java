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
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find book by title", key = "bbyt")
    public String findBookByTitle(String title) {
        return bookService.findByTitle(title)
                .map(bookConverter::bookToString)
                .orElse("Book with title %s not found".formatted(title));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete book by id", key = "bdelbyid")
    public String deleteBook(long id) {
        bookService.deleteById(id);
        return "Book with id %d deleted successfully".formatted(id);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete book by title", key = "bdelbyt")
    public void deleteBook(String title) {
        bookService.deleteByTitle(title);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count book by author id", key = "cbbyaid")
    public int countBooksByAuthorId(long authorId) {
        return bookService.countByAuthorId(authorId);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count book by author full name", key = "cbbyafn")
    public int countBooksByAuthorFullName(String authorFullName) {
        return bookService.countByAuthorFullName(authorFullName);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count book by genre id", key = "cbbygid")
    public int countBooksByGenreId(long genreId) {
        return bookService.countByGenreId(genreId);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count book by genre name", key = "cbbygn")
    public int countBooksByGenreName(String genreName) {
        return bookService.countByGenreName(genreName);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find book by example", key = "bbye")
    public String findBookByExample(Book book) {
        return bookConverter.bookToString(bookService.findByExample(book));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Save book batch", key = "binsb")
    public String saveBookBatch(Set<Book> books) {
        List<Book> savedBooks = bookService.saveBatch(books);
        if (books.isEmpty()) {
            return "No books saved";
        }
        return savedBooks.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete books by ids", key = "bdelbyids")
    public String deleteBooksByIds(Set<Long> ids) {
        bookService.deleteAllByIds(ids);
        return "Books deleted successfully";
    }
}
