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

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.*;

@DataJpaTest
class BookRepositoryTest {

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

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbBooks")
    void shouldReturnCorrectBookByTitle(Book dbBook) {
        var optionalActualBook = bookRepository.findByTitle(dbBook.getTitle());
        var expectedBook = testEntityManager.find(Book.class, dbBook.getId());
        assertThat(optionalActualBook).isPresent();
        var actualBook = optionalActualBook.get();
        assertThatActualAndExpectedBookAreEqual(actualBook, expectedBook);
    }

    @Test
    void shouldDeleteBookByTitle() {
        assertThat(bookRepository.findById(dbBooks.get(0).getId())).isPresent();
        bookRepository.deleteByTitle("BookTitle_1");
        assertThat(bookRepository.findById(dbBooks.get(0).getId())).isEmpty();
    }

    @Test
    void shouldReturnCountByAuthorId() {
        assertThat(bookRepository.countByAuthorId(dbAuthors.get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByAuthorFullName() {
        assertThat(bookRepository.countByAuthorFullName("Author_1")).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreId() {
        assertThat(bookRepository.countByGenresId(dbGenres.get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldReturnCountByGenreName() {
        assertThat(bookRepository.countByGenresName("Genre_1")).isEqualTo(1);
    }
}
