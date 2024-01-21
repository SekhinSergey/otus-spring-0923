package ru.otus.spring.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.model.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.assertThatActualAndExpectedAuthorAreEqual;

@DataMongoTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @ParameterizedTest
    @SuppressWarnings("all")
    @MethodSource("ru.otus.spring.repository.TestBookUtils#getDbAuthors")
    void shouldFindExpectedAuthorByFullName(Author dbAuthor) {
        var optionalActualAuthor = authorRepository.findByFullName(dbAuthor.getFullName());
        var expectedAuthor = mongoTemplate.findById(dbAuthor.getId(), Author.class);
        assertThat(optionalActualAuthor).isPresent();
        var actualAuthor = optionalActualAuthor.get();
        assertThatActualAndExpectedAuthorAreEqual(actualAuthor, expectedAuthor);
    }
}
