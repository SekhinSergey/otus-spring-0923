package ru.otus.spring.service;

import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(String id);

    List<Comment> findAllByBookId(String bookId);

    void deleteAllByBookId(String bookId);

    int countByBookId(String bookId);

    Comment create(Comment comment);

    Comment update(Comment comment);

    List<Comment> createBatch(Set<Comment> comments);

    List<Comment> updateBatch(Set<Comment> comments);

    void deleteAllByIds(Set<String> ids);
}
