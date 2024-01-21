package ru.otus.spring.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.model.Genre;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.assertThatActualAndExpectedGenreAreEqual;

@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @ParameterizedTest
    @SuppressWarnings("all")
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbGenres")
    void shouldFindExpectedGenreById(Genre dbGenre) {
        String id = dbGenre.getId();
        HashSet<String> ids = new HashSet<>();
        ids.add(dbGenre.getId());
        var actualGenre = genreRepository.findAllByIdIn(ids).get(0);
        var expectedGenre = mongoTemplate.findById(id, Genre.class);
        assertThatActualAndExpectedGenreAreEqual(actualGenre, expectedGenre);
    }

    @ParameterizedTest
    @SuppressWarnings("all")
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbGenres")
    void shouldFindExpectedGenreByName(Genre dbGenre) {
        var actualOptionalGenre = genreRepository.findByName(dbGenre.getName());
        var expectedGenre = mongoTemplate.findById(dbGenre.getId(), Genre.class);
        assertThat(actualOptionalGenre).isPresent();
        var actualGenre = actualOptionalGenre.get();
        assertThatActualAndExpectedGenreAreEqual(actualGenre, expectedGenre);
    }
}
