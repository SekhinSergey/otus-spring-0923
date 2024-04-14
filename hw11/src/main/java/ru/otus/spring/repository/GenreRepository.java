package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.model.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}
