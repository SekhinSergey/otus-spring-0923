package ru.otus.spring.service;

import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("all")
public interface BookService {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    List<Book> findAllByIds(Set<Long> ids);

    Book create(Book book);

    Book update(Book book);

    void deleteById(Long id);

    int countByAuthorId(long authorId);

    int countByGenreId(long genreId);

    List<Book> createBatch(Set<Book> books);

    List<Book> updateBatch(Set<Book> books);

    void deleteAllByIds(Set<Long> ids);
}
