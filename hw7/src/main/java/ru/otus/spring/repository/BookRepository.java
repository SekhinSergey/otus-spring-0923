package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"author"})
    @Query("select b from Book b left join fetch b.genres")
    List<Book> findAll();

    @Override
    @EntityGraph(attributePaths = {"author"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"author"})
    Optional<Book> findByTitle(String title);

    void deleteByTitle(String title);

    int countByAuthorId(long authorId);

    int countByAuthorFullName(String authorFullName);

    int countByGenresId(long genreId);

    int countByGenresName(String genreName);
}
