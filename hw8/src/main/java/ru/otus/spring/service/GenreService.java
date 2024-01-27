package ru.otus.spring.service;

import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

    List<Genre> findAll();

    List<Genre> findAllByIds(Set<String> ids);

    Genre create(Genre genre);

    Genre update(Genre genre);

    List<Genre> createBatch(Set<Genre> genres);

    List<Genre> updateBatch(Set<Genre> genres);
}
