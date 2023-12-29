package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.model.Genre;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.getDbGenres;

@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    private List<Genre> dbGenres;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @Test
    void shouldFindExpectedAllGenres() {
        var actualGenres = jpaGenreRepository.findAll();
        var expectedGenres = dbGenres;
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbGenres")
    void shouldFindExpectedGenreById(Genre dbGenre) {
        long id = dbGenre.getId();
        HashSet<Long> ids = new HashSet<>();
        ids.add(dbGenre.getId());
        var actualGenre = jpaGenreRepository.findAllByIds(ids).get(0);
        var expectedGenre = testEntityManager.find(Genre.class, id);
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbGenres")
    void shouldFindExpectedGenreByName(Genre dbGenre) {
        var actualGenre = jpaGenreRepository.findByName(dbGenre.getName());
        var expectedGenre = testEntityManager.find(Genre.class, dbGenre.getId());
        assertThat(actualGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldPersistExpectedGenre() {
        shouldInsertExpectedGenre(0);
    }

    @Test
    void shouldMergeExpectedGenre() {
        shouldInsertExpectedGenre(7);
    }

    void shouldInsertExpectedGenre(long id) {
        var actualGenre = jpaGenreRepository.save(new Genre(id, "Genre_7"));
        var expectedGenre = testEntityManager.find(Genre.class, 7);
        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }
}
