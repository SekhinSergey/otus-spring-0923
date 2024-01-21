package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByIdIn(Set<Long> ids);

    Optional<Genre> findByName(String name);
}
