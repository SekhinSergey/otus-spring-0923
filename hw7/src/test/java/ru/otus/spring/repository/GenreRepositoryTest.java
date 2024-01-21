package ru.otus.spring.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.model.Genre;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.*;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbGenres")
    void shouldFindExpectedGenreById(Genre dbGenre) {
        long id = dbGenre.getId();
        HashSet<Long> ids = new HashSet<>();
        ids.add(dbGenre.getId());
        var actualGenre = genreRepository.findAllByIdIn(ids).get(0);
        var expectedGenre = testEntityManager.find(Genre.class, id);
        assertThatActualAndExpectedGenreAreEqual(actualGenre, expectedGenre);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbGenres")
    void shouldFindExpectedGenreByName(Genre dbGenre) {
        var actualOptionalGenre = genreRepository.findByName(dbGenre.getName());
        var expectedGenre = testEntityManager.find(Genre.class, dbGenre.getId());
        assertThat(actualOptionalGenre).isPresent();
        var actualGenre = actualOptionalGenre.get();
        assertThatActualAndExpectedGenreAreEqual(actualGenre, expectedGenre);
    }
}
