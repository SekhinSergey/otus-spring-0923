package ru.otus.spring.service;

import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {

    List<Genre> findAll();

    List<Genre> findAllByIds(Set<Long> ids);

    Optional<Genre> findByName(String name);

    Genre insert(Genre genre);
}
