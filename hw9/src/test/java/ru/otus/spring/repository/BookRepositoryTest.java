package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.*;

@DataJpaTest
class BookRepositoryTest {

    private static final String SIZE_ASSERTION_RULE = "java:S5838";

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @Test
    @SuppressWarnings(SIZE_ASSERTION_RULE)
    void shouldReturnCorrectBookList() {
        var actualBookList = bookRepository.findAll();
        var expectedBookList = dbBooks;
        assertThat(actualBookList.size()).isEqualTo(expectedBookList.size());
        var expectedBookMap = expectedBookList.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        actualBookList.forEach(actualBook -> {
            var expectedBook = expectedBookMap.get(actualBook.getId());
            assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
        });
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbBooks")
    void shouldReturnCorrectBookById(Book dbBook) {
        var optionalActualBook = bookRepository.findById(dbBook.getId());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(optionalActualBook).isPresent();
        var actualBook = optionalActualBook.get();
        assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
    }

    @Test
    void shouldReturnCountByAuthorId() {
        assertThat(bookRepository.countByAuthorId(dbAuthors.get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreId() {
        assertThat(bookRepository.countByGenresId(dbGenres.get(0).getId())).isEqualTo(1);
    }
}
