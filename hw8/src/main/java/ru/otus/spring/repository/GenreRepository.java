package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends MongoRepository<Genre, String> {

    List<Genre> findAllByIdIn(Set<String> ids);

    Optional<Genre> findByName(String name);
}
