package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.model.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByTitle(String title);

    void deleteByTitle(String title);

    int countByAuthorId(String authorId);

    int countByGenresId(String genreId);


    // Считают неверно. Не нашел решения. Тесты закоментил

    @Query(value = "{'author.full_name'::#{#authorFullName}}", count = true)
    int countByAuthorFullName(@Param("author.full_name") String authorFullName);

    @Query(value = "{'genre.name'::#{#genreName}}", count = true)
    int countByGenresName(@Param("genre.name") String genreName);
}
