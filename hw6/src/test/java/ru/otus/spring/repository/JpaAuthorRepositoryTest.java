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
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.assertThatActualAndExpectedAuthorAreEqual;
import static ru.otus.spring.repository.TestBookUtils.getDbAuthors;

@DataJpaTest
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {

    private static final String SIZE_ASSERTION_RULE = "java:S5838";

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
    @SuppressWarnings(SIZE_ASSERTION_RULE)
    void shouldFindExpectedAllAuthors() {
        var actualAuthorList = jpaAuthorRepository.findAll();
        var expectedAuthorList = dbAuthors;
        assertThat(actualAuthorList.size()).isEqualTo(expectedAuthorList.size());
        var expectedAuthorMap = expectedAuthorList.stream()
                .collect(Collectors.toMap(Author::getId, Function.identity()));
        actualAuthorList.forEach(actualAuthor -> {
            var expectedAuthor = expectedAuthorMap.get(actualAuthor.getId());
            assertThatActualAndExpectedAuthorAreEqual(actualAuthor, expectedAuthor);
        });
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbAuthors")
    void shouldFindExpectedAuthorById(Author dbAuthor) {
        long id = dbAuthor.getId();
        var optionalActualAuthor = jpaAuthorRepository.findById(id);
        var expectedAuthor = testEntityManager.find(Author.class, id);
        assertThat(optionalActualAuthor).isPresent();
        var actualAuthor = optionalActualAuthor.get();
        assertThatActualAndExpectedAuthorAreEqual(actualAuthor, expectedAuthor);
    }

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbAuthors")
    void shouldFindExpectedAuthorByFullName(Author dbAuthor) {
        var optionalActualAuthor = jpaAuthorRepository.findByFullName(dbAuthor.getFullName());
        var expectedAuthor = testEntityManager.find(Author.class, dbAuthor.getId());
        assertThat(optionalActualAuthor).isPresent();
        var actualAuthor = optionalActualAuthor.get();
        assertThatActualAndExpectedAuthorAreEqual(actualAuthor, expectedAuthor);
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
        var actualAuthor = jpaAuthorRepository.save(new Author(id, "Author_4"));
        var expectedAuthor = testEntityManager.find(Author.class, 4);
        assertThatActualAndExpectedAuthorAreEqual(actualAuthor, expectedAuthor);
    }
}
