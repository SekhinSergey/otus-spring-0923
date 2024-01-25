package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByBookId(String bookId);

    void deleteAllByBookId(String bookId);

    @DeleteQuery("book.ids")
    void deleteAllByBookIdIn(Set<String> bookIds);

    int countByBookId(String bookId);
}
