package ru.otus.spring.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.model.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.assertThatActualAndExpectedAuthorAreEqual;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @ParameterizedTest
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbAuthors")
    void shouldFindExpectedAuthorByFullName(Author dbAuthor) {
        var optionalActualAuthor = authorRepository.findByFullName(dbAuthor.getFullName());
        var expectedAuthor = testEntityManager.find(Author.class, dbAuthor.getId());
        assertThat(optionalActualAuthor).isPresent();
        var actualAuthor = optionalActualAuthor.get();
        assertThatActualAndExpectedAuthorAreEqual(actualAuthor, expectedAuthor);
    }
}
