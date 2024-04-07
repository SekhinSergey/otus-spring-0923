package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.model.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findAllByBookId(String bookId);

    Mono<Comment> findFirstByOrderByIdDesc();

    Mono<Void> deleteAllByBookId(String bookId);
}
