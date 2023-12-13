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
import ru.otus.spring.model.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    private List<Author> dbAuthors;

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @Test
    void shouldFindExpectedAllAuthors() {
        var actualAuthors = authorRepositoryJpa.findAll();
        var expectedAuthors = dbAuthors;
        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldFindExpectedAuthorById(Author dbAuthor) {
        long id = dbAuthor.getId();
        var actualAuthor = authorRepositoryJpa.findById(id);
        var expectedAuthor = testEntityManager.find(Author.class, id);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldFindExpectedAuthorByFullName(Author dbAuthor) {
        var actualAuthor = authorRepositoryJpa.findByFullName(dbAuthor.getFullName());
        var expectedAuthor = testEntityManager.find(Author.class, dbAuthor.getId());
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @Test
    void shouldPersistExpectedAuthor() {
        shouldSaveExpectedAuthor(0);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldMergeExpectedAuthor() {
        shouldSaveExpectedAuthor(4);
    }

    void shouldSaveExpectedAuthor(long id) {
        var actualAuthor = authorRepositoryJpa.insert(new Author(id, "Author_4"));
        var expectedAuthor = testEntityManager.find(Author.class, 4);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Author((long) id, "Author_" + id))
                .toList();
    }
}
