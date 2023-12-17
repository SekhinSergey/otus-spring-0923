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

    Optional<Comment> insert(Comment comment);

    Optional<Comment> updateTextById(long id, String text);

    Optional<Comment> updateTextByBookId(long bookId, String text);

    Optional<Comment> updateTextByBookTitle(String title, String text);

    void deleteAllByBookId(long bookId);

    void deleteAllByBookTitle(String title);

    int countByBookId(long bookId);

    int countByBookTitle(String title);
}
