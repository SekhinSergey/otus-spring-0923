package ru.otus.spring.repository;

import org.assertj.core.api.Assertions;
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
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.getDbAuthors;
import static ru.otus.spring.repository.TestBookUtils.getDbBooks;
import static ru.otus.spring.repository.TestBookUtils.getDbGenres;

@DataJpaTest
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {

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
        var actualBook = jpaBookRepository.findByTitle(dbBook.getTitle());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbBooks")
    void shouldReturnCorrectBookById(Book dbBook) {
        var actualBook = jpaBookRepository.findById(dbBook.getId());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @Test
    @SuppressWarnings("all")
    void shouldReturnCorrectBookList() {
        var actualBooks = jpaBookRepository.findAll();
        var expectedBooks = dbBooks;
        IntStream.range(0, actualBooks.size())
                .mapToObj(i -> actualBooks.get(i).equals(expectedBooks.get(i)))
                .forEach(Assertions::assertThat);
    }

    @Test
    void shouldPersistNewBookCorrectly() {
        shouldSaveBookCorrectly(null);
    }

    @Test
    void shouldMergeNewBookCorrectly() {
        shouldSaveBookCorrectly(4L);
    }

    @SuppressWarnings("all")
    void shouldSaveBookCorrectly(Long id) {
        var expectedBook = new Book(
                id,
                "BookTitle_10500",
                dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
        assertThat(jpaBookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    @SuppressWarnings("all")
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(
                dbBooks.get(0).getId(),
                "BookTitle_10500",
                dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));
        Optional<Book> beforeUpdateBook = jpaBookRepository.findById(expectedBook.getId());
        assertThat(beforeUpdateBook)
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);
        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
        assertThat(jpaBookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
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
