package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.*;

@DataJpaTest
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {

    private static final String SIZE_ASSERTION_RULE = "java:S5838";

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbBooks")
    void shouldReturnCorrectBookByTitle(Book dbBook) {
        var optionalActualBook = jpaBookRepository.findByTitle(dbBook.getTitle());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(optionalActualBook).isPresent();
        var actualBook = optionalActualBook.get();
        assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbBooks")
    void shouldReturnCorrectBookById(Book dbBook) {
        var optionalActualBook = jpaBookRepository.findById(dbBook.getId());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(optionalActualBook).isPresent();
        var actualBook = optionalActualBook.get();
        assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
    }

    @Test
    @SuppressWarnings(SIZE_ASSERTION_RULE)
    void shouldReturnCorrectBookList() {
        var actualBookList = jpaBookRepository.findAll();
        var expectedBookList = dbBooks;
        assertThat(actualBookList.size()).isEqualTo(expectedBookList.size());
        var expectedBookMap = expectedBookList.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        actualBookList.forEach(actualBook -> {
            var expectedBook = expectedBookMap.get(actualBook.getId());
            assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
        });
    }

    @Test
    void shouldPersistNewBookCorrectly() {
        var expectedBook = new Book(
                null,
                "BookTitle_10500",
                dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        shouldSaveBook(expectedBook);
    }

    @Test
    void shouldSaveUpdatedBook() {
        var expectedOldBook = new Book(
                dbBooks.get(0).getId(),
                "BookTitle_1",
                dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));
        var optionalFoundBook = jpaBookRepository.findById(expectedOldBook.getId());
        assertThat(optionalFoundBook).isPresent();
        var foundBook = optionalFoundBook.get();
        assertThatActualAndExpectedBookAreEqual(foundBook, expectedOldBook);
        var expectedNewBook = new Book(
                dbBooks.get(0).getId(),
                "BookTitle_4",
                dbAuthors.get(1),
                List.of(dbGenres.get(2), dbGenres.get(3)));
        shouldSaveBook(expectedNewBook);
    }

    private void shouldSaveBook(Book expectedBook) {
        var savedBook = jpaBookRepository.save(expectedBook);
        assertThat(savedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0);
        assertThatActualAndExpectedBookAreEqual(savedBook, expectedBook);
        var optionalFoundSavedBook = jpaBookRepository.findById(savedBook.getId());
        assertThat(optionalFoundSavedBook).isPresent();
        var foundSavedBook = optionalFoundSavedBook.get();
        assertThatActualAndExpectedBookAreEqual(foundSavedBook, savedBook);
    }

    @Test
    void shouldDeleteBookById() {
        assertThat(jpaBookRepository.findById(dbBooks.get(0).getId())).isPresent();
        jpaBookRepository.deleteById(dbBooks.get(0).getId());
        assertThat(jpaBookRepository.findById(dbBooks.get(0).getId())).isEmpty();
    }

    @Test
    void shouldDeleteBookByTitle() {
        assertThat(jpaBookRepository.findById(dbBooks.get(0).getId())).isPresent();
        jpaBookRepository.deleteByTitle("BookTitle_1");
        assertThat(jpaBookRepository.findById(dbBooks.get(0).getId())).isEmpty();
    }

    @Test
    void shouldReturnCountByAuthorId() {
        assertThat(jpaBookRepository.countByAuthorId(dbAuthors.get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByAuthorFullName() {
        assertThat(jpaBookRepository.countByAuthorFullName("Author_1")).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreId() {
        assertThat(jpaBookRepository.countByGenreId(dbGenres.get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreName() {
        assertThat(jpaBookRepository.countByGenreName("Genre_1")).isEqualTo(1);
    }
}
