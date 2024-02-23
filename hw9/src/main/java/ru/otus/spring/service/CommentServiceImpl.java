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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.otus.spring.constant.Constants.BOOKS_SIZE_ERROR_MESSAGE;
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
        long bookId = commentCreateDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId)));
        return commentMapper.toCreateDto(
                commentRepository.save(commentMapper.createDtoToEntity(commentCreateDto, book)));
    }

    @Override
    @Transactional
    public CommentUpdateDto update(CommentUpdateDto commentUpdateDto) {
        Long id = commentUpdateDto.getId();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id)));
        long bookId = commentUpdateDto.getBookId();
        bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId)));
        String text = commentUpdateDto.getText();
        if (text.equals(comment.getText())) {
            return commentMapper.toUpdateDto(comment);
        } else {
            comment.setText(text);
        }
        return commentMapper.toUpdateDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public List<CommentCreateDto> createBatch(Set<CommentCreateDto> commentCreateDtos) {
        Set<Long> bookIds = commentCreateDtos.stream()
                .map(CommentCreateDto::getBookId)
                .collect(Collectors.toSet());
        Map<Long, Book> bookByBookIdMap = bookRepository.findAllById(bookIds).stream()
                .collect(Collectors.toMap(Book::getId, book -> book, (a, b) -> b));
        if (bookIds.size() != bookByBookIdMap.size()) {
            throw new NotFoundException(BOOKS_SIZE_ERROR_MESSAGE);
        }
        Set<Comment> comments = commentCreateDtos.stream()
                .map(dto -> Comment.builder()
                        .text(dto.getText())
                        .book(bookByBookIdMap.get(dto.getBookId()))
                        .build())
                .collect(Collectors.toSet());
        return commentRepository.saveAll(comments).stream()
                .map(commentMapper::toCreateDto)
                .toList();
    }

    @Override
    @Transactional
    public List<CommentUpdateDto> updateBatch(Set<CommentUpdateDto> commentUpdateDtos) {
        Set<Long> commentIds = commentUpdateDtos.stream()
                .map(CommentUpdateDto::getId)
                .collect(Collectors.toCollection(HashSet::new));
        if (commentIds.size() != commentRepository.findAllById(commentIds).size()) {
            throw new NotFoundException(
                    "The number of requested foundComments does not match the number in the database");
        }
        Set<Long> bookIds = commentUpdateDtos.stream()
                .map(CommentUpdateDto::getBookId)
                .collect(Collectors.toSet());
        Map<Long, Book> bookByBookIdMap = bookRepository.findAllById(bookIds).stream()
                .collect(Collectors.toMap(Book::getId, book -> book, (a, b) -> b));
        if (bookIds.size() != bookByBookIdMap.size()) {
            throw new NotFoundException(BOOKS_SIZE_ERROR_MESSAGE);
        }
        Set<Comment> comments = getFormedCommentsForUpdate(commentUpdateDtos, bookByBookIdMap);
        return commentRepository.saveAll(comments).stream()
                .map(commentMapper::toUpdateDto)
                .toList();
    }

    private static Set<Comment> getFormedCommentsForUpdate(Set<CommentUpdateDto> commentUpdateDtos,
                                                           Map<Long, Book> bookByBookIdMap) {
        return commentUpdateDtos.stream()
                .map(dto -> Comment.builder()
                        .id(dto.getId())
                        .text(dto.getText())
                        .book(bookByBookIdMap.get(dto.getBookId()))
                        .build())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<Long> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
    }
}
