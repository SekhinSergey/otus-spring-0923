package ru.otus.spring.service;

import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findById(long id);

    Optional<Book> findByTitle(String title);

    List<Book> findAll();

    Book insert(String title, long authorId, List<Long> genresIds);

    Book update(long id, String title, long authorId, List<Long> genresIds);

    void deleteById(long id);

    int countByAuthorId(long authorId);

    int countByAuthorFullName(String authorFullName);

    int countByGenreId(long genreId);

    int countByGenreName(String genreName);
}
