package ru.otus.spring.repository;

import ru.otus.spring.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    List<Author> findAll();

    Optional<Author> findById(long id);

    Optional<Author> findByFullName(String fullName);

    Author save(Author author);
}
