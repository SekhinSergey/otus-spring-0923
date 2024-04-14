package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static ru.otus.spring.utils.EntityUtils.getThirdBook;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    void shouldGetLastBook() {
        StepVerifier
                .create(repository.findFirstByOrderByIdDesc())
                .assertNext(book -> getThirdBook())
                .expectComplete()
                .verify();
    }
}
