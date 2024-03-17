package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
