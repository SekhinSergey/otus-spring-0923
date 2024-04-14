package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
