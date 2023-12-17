package ru.otus.spring.repository;

import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> findById(long id);

    Optional<Book> findByTitle(String title);

    List<Book> findAll();

    Optional<Book> save(Book book);

    void deleteById(long id);

    void deleteByTitle(String title);

    int countByAuthorId(long authorId);

    int countByAuthorFullName(String authorFullName);

    int countByGenreId(long genreId);

    int countByGenreName(String genreName);
}
