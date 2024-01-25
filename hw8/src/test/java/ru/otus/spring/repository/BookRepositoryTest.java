package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.getDbAuthors;
import static ru.otus.spring.repository.TestBookUtils.getDbGenres;

@DataMongoTest
class BookRepositoryTest {

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
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
