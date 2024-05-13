package ru.otus.spring.service;

import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAllByBookId(long bookId);

    void deleteById(long id);

    CommentDto create(CommentCreateDto commentCreateDto);

    CommentDto update(long id, CommentUpdateDto commentUpdateDto);
}
