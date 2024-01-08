package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFullName(String fullName);
}
