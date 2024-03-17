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

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.assertThatActualAndExpectedBookAreEqual;
import static ru.otus.spring.utils.Utils.getDbAuthors;
import static ru.otus.spring.utils.Utils.getDbBooks;
import static ru.otus.spring.utils.Utils.getDbGenres;

@DataJpaTest
class BookRepositoryTest {

    private static final int SINGLE_SIZE = 1;

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
    void shouldReturnCorrectBookList() {
        var actualBookList = bookRepository.findAll();
        var expectedBookList = dbBooks;
        assertThat(actualBookList.size()).isEqualTo(expectedBookList.size());
        var expectedBookMap = expectedBookList.stream()
                .collect(toMap(Book::getId, Function.identity()));
        actualBookList.forEach(actualBook -> {
            var expectedBook = expectedBookMap.get(actualBook.getId());
            assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
        });
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.utils.Utils#getDbBooks")
    void shouldReturnCorrectBookById(Book dbBook) {
        var optionalActualBook = bookRepository.findById(dbBook.getId());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(optionalActualBook).isPresent();
        var actualBook = optionalActualBook.get();
        assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
    }

    @Test
    void shouldReturnCountByAuthorId() {
        assertThat(bookRepository.countByAuthorId(dbAuthors.get(FIRST_INDEX).getId())).isEqualTo(SINGLE_SIZE);
    }

    @Test
    void shouldReturnCountByGenreId() {
        assertThat(bookRepository.countByGenresId(dbGenres.get(FIRST_INDEX).getId())).isEqualTo(SINGLE_SIZE);
    }
}
