package ru.otus.spring.repository;

import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(long id);

    Optional<Comment> findByText(String text);

    List<Comment> findAllByBookId(long bookId);

    Optional<Comment> insert(Comment comment);

    void updateTextById(long id, String text);

    void deleteById(long id);
}
