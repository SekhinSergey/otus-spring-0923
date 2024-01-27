package ru.otus.spring.service;

import ru.otus.spring.model.Author;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    List<Author> findAll();

    Author findById(String id);

    Author create(Author author);

    Author update(Author author);

    List<Author> createBatch(Set<Author> authors);

    List<Author> updateBatch(Set<Author> authors);
}
