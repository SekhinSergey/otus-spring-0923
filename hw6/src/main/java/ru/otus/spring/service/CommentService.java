package ru.otus.spring.service;

import ru.otus.spring.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(long id);

    Comment findByText(String text);

    List<Comment> findAllByBookId(long bookId);

    List<Comment> findAllByBookTitle(String title);

    Comment insert(Comment comment);

    Comment updateTextById(long id, String text);

    Comment updateTextByBookId(long bookId, String text);

    Comment updateTextByBookTitle(String title, String text);

    void deleteAllByBookId(long bookId);

    void deleteAllByBookTitle(String title);

    int countByBookId(long bookId);

    int countByBookTitle(String title);
}
