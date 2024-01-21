package ru.otus.spring.service;

import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<Book> findById(String id);

    Optional<Book> findByTitle(String title);

    List<Book> findAll();

    Book insert(String title, String authorId, Set<String> genresIds);

    Book update(String id, String title, String authorId, Set<String> genresIds);

    void deleteById(String id);

    void deleteByTitle(String title);

    int countByAuthorId(String authorId);

    int countByAuthorFullName(String authorFullName);

    int countByGenreId(String genreId);

    int countByGenreName(String genreName);

    Book findByExample(Book book);

    List<Book> saveBatch(Set<Book> books);

    void deleteAllByIds(Set<String> ids);
}
