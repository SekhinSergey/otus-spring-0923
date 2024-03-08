package ru.otus.spring.service;

import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;

import java.util.List;
import java.util.Set;

public interface CommentService {

    List<CommentDto> findAll();

    CommentDto findById(long id);

    List<CommentDto> findAllByIds(Set<Long> ids);

    List<CommentDto> findAllByBookId(long bookId);

    void deleteById(long id);

    void deleteAllByBookId(long bookId);

    int countByBookId(long bookId);

    CommentDto create(CommentCreateDto commentCreateDto);

    CommentDto update(CommentUpdateDto commentUpdateDto);

    List<CommentDto> createBatch(Set<CommentCreateDto> commentCreateDtos);

    List<CommentDto> updateBatch(Set<CommentUpdateDto> commentUpdateDtos);

    void deleteAllByIds(Set<Long> ids);
}
