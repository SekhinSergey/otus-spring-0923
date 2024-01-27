package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {

    int countByAuthorId(String authorId);

    int countByGenresId(String genreId);
}
