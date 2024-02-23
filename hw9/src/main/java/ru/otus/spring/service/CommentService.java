package ru.otus.spring.service;

import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;

import java.util.List;
import java.util.Set;

public interface CommentService {

    List<CommentDto> findAll();

    CommentDto findById(Long id);

    CommentUpdateDto findByIdForEditing(Long id);

    List<CommentDto> findAllByIds(Set<Long> ids);

    List<CommentDto> findAllByBookId(Long bookId);

    void deleteById(Long id);

    void deleteAllByBookId(Long bookId);

    int countByBookId(Long bookId);

    CommentCreateDto create(CommentCreateDto commentCreateDto);

    CommentUpdateDto update(CommentUpdateDto commentUpdateDto);

    List<CommentCreateDto> createBatch(Set<CommentCreateDto> commentCreateDtos);

    List<CommentUpdateDto> updateBatch(Set<CommentUpdateDto> commentUpdateDtos);

    void deleteAllByIds(Set<Long> ids);
}
