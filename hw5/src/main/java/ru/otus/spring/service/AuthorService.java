package ru.otus.spring.service;

import ru.otus.spring.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author findById(long id);

    Author findByFullName(String fullName);

    Author insert(Author author);
}
