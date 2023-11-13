package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({BookRepositoryJdbc.class, GenreRepositoryJdbc.class, AuthorRepositoryJdbc.class})
class BookRepositoryJdbcTest {

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @Autowired
    private BookRepositoryJdbc repositoryJdbc;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookByTitle(Book expectedBook) {
        var actualBook = repositoryJdbc.findByTitle(expectedBook.getTitle());
        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repositoryJdbc.findById(expectedBook.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnCorrectBookList() {
        var actualBooks = repositoryJdbc.findAll();
        var expectedBooks = dbBooks;
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(
                0,
                "BookTitle_10500",
                dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
        assertThat(repositoryJdbc.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(
                1L,
                "BookTitle_10500",
                dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));
        assertThat(repositoryJdbc.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);
        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
        assertThat(repositoryJdbc.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    void shouldDeleteBookById() {
        assertThat(repositoryJdbc.findById(1L)).isPresent();
        repositoryJdbc.deleteById(1L);
        assertThat(repositoryJdbc.findById(1L)).isEmpty();
    }

    @Test
    void shouldReturnCountByAuthorId() {
        assertThat(repositoryJdbc.countByAuthorId(1L)).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByAuthorFullName() {
        assertThat(repositoryJdbc.countByAuthorFullName("Author_1")).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreId() {
        assertThat(repositoryJdbc.countByGenreId(1L)).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreName() {
        assertThat(repositoryJdbc.countByGenreName("Genre_1")).isEqualTo(1);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                .boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Book(
                        id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
