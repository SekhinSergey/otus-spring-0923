package ru.otus.spring.repository;

import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);

    Optional<Genre> findByName(String name);

    Genre insert(Genre genre);
}
