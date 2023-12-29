package ru.otus.spring.repository;

import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(long id);

    Optional<Comment> findByText(String text);

    List<Comment> findAllByBookId(long bookId);

    List<Comment> findAllByBookTitle(String title);

    Comment save(Comment comment);

    void deleteAllByBookId(long bookId);

    void deleteAllByBookTitle(String title);

    int countByBookId(long bookId);

    int countByBookTitle(String title);
}
