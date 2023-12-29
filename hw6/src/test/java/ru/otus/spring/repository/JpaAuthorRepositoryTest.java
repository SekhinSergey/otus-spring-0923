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

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.getDbAuthors;

@DataJpaTest
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {

    private List<Author> dbAuthors;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @Test
    void shouldFindExpectedAllAuthors() {
        var actualAuthors = jpaAuthorRepository.findAll();
        var expectedAuthors = dbAuthors;
        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbAuthors")
    void shouldFindExpectedAuthorById(Author dbAuthor) {
        long id = dbAuthor.getId();
        var actualAuthor = jpaAuthorRepository.findById(id);
        var expectedAuthor = testEntityManager.find(Author.class, id);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbAuthors")
    void shouldFindExpectedAuthorByFullName(Author dbAuthor) {
        var actualAuthor = jpaAuthorRepository.findByFullName(dbAuthor.getFullName());
        var expectedAuthor = testEntityManager.find(Author.class, dbAuthor.getId());
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @Test
    void shouldPersistExpectedAuthor() {
        shouldInsertExpectedAuthor(0);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldMergeExpectedAuthor() {
        shouldInsertExpectedAuthor(4);
    }

    void shouldInsertExpectedAuthor(long id) {
        var actualAuthor = jpaAuthorRepository.save(new Author(id, "Author_4"));
        var expectedAuthor = testEntityManager.find(Author.class, 4);
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }
}
