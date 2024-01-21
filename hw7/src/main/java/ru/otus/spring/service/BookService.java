package ru.otus.spring.service;

import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<Book> findById(Long id);

    Optional<Book> findByTitle(String title);

    List<Book> findAll();

    Book insert(String title, long authorId, Set<Long> genresIds);

    Book update(Long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(Long id);

    void deleteByTitle(String title);

    int countByAuthorId(long authorId);

    int countByAuthorFullName(String authorFullName);

    int countByGenreId(long genreId);

    int countByGenreName(String genreName);

    Book findByExample(Book book);

    List<Book> saveBatch(Set<Book> books);

    void deleteAllByIds(Set<Long> ids);
}
