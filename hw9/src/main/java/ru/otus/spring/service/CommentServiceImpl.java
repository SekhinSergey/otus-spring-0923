package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.mapper.CommentMapper;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.otus.spring.constant.Constants.NO_BOOK_BY_ID_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(Long id) {
        return commentMapper.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id))));
    }

    @Override
    public CommentUpdateDto findByIdForEditing(Long id) {
        return commentMapper.toUpdateDto(commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id))));
    }

    @Override
    public List<CommentDto> findAllByIds(Set<Long> ids) {
        return commentRepository.findAllById(ids).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByBookId(Long bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByBookId(Long bookId) {
        return commentRepository.countByBookId(bookId);
    }

    @Override
    @Transactional
    public CommentCreateDto create(CommentCreateDto commentCreateDto) {
        return commentMapper.toCreateDto(commentRepository.save(createDtoToEntity(commentCreateDto)));
    }

    @Override
    @Transactional
    public CommentUpdateDto update(CommentUpdateDto commentUpdateDto) {
        validateComments(Collections.singleton(commentUpdateDto));
        return commentMapper.toUpdateDto(commentRepository.save(updateDtoToEntity(commentUpdateDto)));
    }

    @Override
    @Transactional
    public List<CommentCreateDto> createBatch(Set<CommentCreateDto> commentCreateDtos) {
        Set<Comment> comments = commentCreateDtos.stream()
                .map(this::createDtoToEntity)
                .collect(Collectors.toSet());
        return commentRepository.saveAll(comments).stream()
                .map(commentMapper::toCreateDto)
                .toList();
    }

    private Comment createDtoToEntity(CommentCreateDto commentCreateDto) {
        long bookId = commentCreateDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId)));
        return commentMapper.createDtoToEntity(commentCreateDto, book);
    }

    @Override
    @Transactional
    public List<CommentUpdateDto> updateBatch(Set<CommentUpdateDto> commentUpdateDtos) {
        validateComments(commentUpdateDtos);
        Set<Comment> comments = commentUpdateDtos.stream()
                .map(this::updateDtoToEntity)
                .collect(Collectors.toSet());
        return commentRepository.saveAll(comments).stream()
                .map(commentMapper::toUpdateDto)
                .toList();
    }

    private Comment updateDtoToEntity(CommentUpdateDto commentUpdateDto) {
        long bookId = commentUpdateDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId)));
        return commentMapper.updateDtoToEntity(commentUpdateDto, book);
    }

    private void validateComments(Set<CommentUpdateDto> commentUpdateDtos) {
        Set<Long> commentsIds = commentUpdateDtos.stream()
                .map(CommentUpdateDto::getId)
                .collect(Collectors.toCollection(HashSet::new));
        List<Comment> foundComments = commentRepository.findAllById(commentsIds);
        if (commentsIds.size() != foundComments.size()) {
            throw new NotFoundException(
                    "The number of requested comments does not match the number in the database");
        }
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<Long> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
    }
}
