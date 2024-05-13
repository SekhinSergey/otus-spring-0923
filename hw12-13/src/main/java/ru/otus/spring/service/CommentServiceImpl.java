package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.UserRepository;

import java.util.List;

import static ru.otus.spring.constant.Constants.NO_BOOK_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_USER_BY_ID_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String NO_COMMENT_BY_ID_ERROR_MESSAGE = "Comment with id %d not found";

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_COMMENT_BY_ID_ERROR_MESSAGE.formatted(id)));
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommentDto create(CommentCreateDto dto) {
        long bookId = dto.bookId();
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId)));
        long userId = dto.userId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NO_USER_BY_ID_ERROR_MESSAGE.formatted(userId)));
        return toDto(commentRepository.save(toEntity(dto, book, user)));
    }

    @Override
    @Transactional
    public CommentDto update(long id, CommentUpdateDto dto) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_COMMENT_BY_ID_ERROR_MESSAGE.formatted(id)));
        long bookId = dto.bookId();
        bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId)));
        long userId = dto.userId();
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NO_USER_BY_ID_ERROR_MESSAGE.formatted(userId)));
        var text = dto.text();
        if (text.equals(comment.getText())) {
            return toDto(comment);
        } else {
            comment.setText(text);
        }
        return toDto(commentRepository.save(comment));
    }

    private CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build();
    }

    private Comment toEntity(CommentCreateDto dto, Book book, User user) {
        return Comment.builder()
                .text(dto.text())
                .book(book)
                .user(user)
                .build();
    }
}
