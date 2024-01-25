package ru.otus.spring.service;

import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<Book> findById(String id);

    List<Book> findAll();

    Book create(Book book);

    Book update(Book book);

    void deleteById(String id);

    int countByAuthorId(String authorId);

    int countByGenreId(String genreId);

    List<Book> createBatch(Set<Book> books);

    List<Book> updateBatch(Set<Book> books);

    void deleteAllByIds(Set<String> ids);
}
