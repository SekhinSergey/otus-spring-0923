package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(value = "Book.author")
    List<Book> findAll();

    @Override
    @EntityGraph(value = "Book.all")
    Optional<Book> findById(Long id);

    int countByAuthorId(long authorId);

    int countByGenresId(long genreId);
}
