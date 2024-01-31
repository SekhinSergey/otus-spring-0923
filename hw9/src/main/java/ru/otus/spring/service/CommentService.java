package ru.otus.spring.service;

import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public interface CommentService {

    List<Comment> findAll();

    Comment findById(Long id);

    List<Comment> findAllByIds(Set<Long> ids);

    List<Comment> findAllByBookId(Long bookId);

    void deleteById(Long id);

    void deleteAllByBookId(Long bookId);

    int countByBookId(Long bookId);

    Comment create(Comment comment);

    Comment update(Comment comment);

    List<Comment> createBatch(Set<Comment> comments);

    List<Comment> updateBatch(Set<Comment> comments);

    void deleteAllByIds(Set<Long> ids);
}
