package ru.otus.spring.service;

import ru.otus.spring.model.Author;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    List<Author> findAll();

    Author findById(String id);

    Author findByFullName(String fullName);

    Author save(Author author);

    Author findByExample(Author author);

    List<Author> saveBatch(Set<Author> authors);
}
