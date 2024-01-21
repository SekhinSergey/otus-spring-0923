package ru.otus.spring.service;

import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(String id);

    Comment findByText(String text);

    List<Comment> findAllByBookId(String bookId);

    List<Comment> findAllByBookTitle(String title);

    Comment save(Comment comment);

    void deleteAllByBookId(String bookId);

    void deleteAllByBookTitle(String title);

    int countByBookId(String bookId);

    int countByBookTitle(String title);

    Comment findByExample(Comment comment);

    List<Comment> saveBatch(Set<Comment> comments);

    void deleteAllByIds(Set<String> ids);
}
