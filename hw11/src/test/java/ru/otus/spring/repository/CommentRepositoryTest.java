package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static ru.otus.spring.utils.EntityUtils.THIRD_ID;
import static ru.otus.spring.utils.EntityUtils.getSecondComment;
import static ru.otus.spring.utils.EntityUtils.getThirdComment;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Test
    void shouldFindAllByLastBookId() {
        StepVerifier
                .create(repository.findAllByBookId(THIRD_ID))
                .assertNext(comment -> getThirdComment())
                .expectComplete()
                .verify();
    }

    @Test
    void shouldFindLastComment() {
        StepVerifier
                .create(repository.findFirstByOrderByIdDesc())
                .assertNext(comment -> getThirdComment())
                .expectComplete()
                .verify();
    }

    @Test
    void shouldDeleteAllByLastBookId() {
        StepVerifier
                .create(repository.deleteAllByBookId(THIRD_ID))
                .expectComplete()
                .verify();
        StepVerifier
                .create(repository.findFirstByOrderByIdDesc())
                .assertNext(comment -> getSecondComment())
                .expectComplete()
                .verify();
    }

}