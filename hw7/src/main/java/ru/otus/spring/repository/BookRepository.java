package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    void deleteByTitle(String title);

    int countByAuthorId(long authorId);

    int countByAuthorFullName(String authorFullName);

    int countByGenresId(long genreId);

    int countByGenresName(String genreName);
}
