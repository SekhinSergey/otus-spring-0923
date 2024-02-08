package ru.otus.spring.service;

import ru.otus.spring.model.Author;
import ru.otus.spring.model.Genre;

import java.util.Set;

public interface BookDtoService {

    Author getAuthorByBookDtoAuthorId(Long id);

    Set<Genre> getGenresByBookDtoGenreIds(Set<Long> ids);

    boolean isTest();
}
