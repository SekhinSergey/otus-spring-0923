package ru.otus.spring.service;

import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {

    List<Genre> findAll();

    List<Genre> findAllByIds(Set<String> ids);

    Optional<Genre> findByName(String name);

    Genre save(Genre genre);

    Optional<Genre> findByExample(Genre genre);

    List<Genre> saveBatch(Set<Genre> genres);
}
