package ru.otus.spring.service;

import ru.otus.spring.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByBookId(long bookId);
}
