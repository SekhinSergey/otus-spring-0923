package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    public Comment findByText(String text) {
        return commentRepository.findByText(text).orElseThrow(() ->
                new EntityNotFoundException("Comment with text %s not found".formatted(text)));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        List<Comment> comments = commentRepository.findAllByBookId(bookId);
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("Comments with book id %d not found".formatted(bookId));
        }
        return comments;
    }

    @Override
    public List<Comment> findAllByBookTitle(String title) {
        List<Comment> comments = commentRepository.findAllByBookTitle(title);
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("Comments with book title %s not found".formatted(title));
        }
        return comments;
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public Comment save(Comment comment) {
        if (isNull(comment.getBook()) || isNull(comment.getBook().getId())) {
            throw new EntityNotFoundException("No book info");
        }
        Long bookId = comment.getBook().getId();
        bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        Comment savedComment = commentRepository.save(comment);
        if (isNull(savedComment)) {
            throw new EntityNotFoundException("An error occurred when trying to save the comment with text %s"
                    .formatted(comment.getText()));
        }
        return findById(comment.getId());
    }

    @Override
    public void deleteAllByBookId(long bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    @Override
    public void deleteAllByBookTitle(String title) {
        commentRepository.deleteAllByBookTitle(title);
    }

    @Override
    public int countByBookId(long bookId) {
        return commentRepository.countByBookId(bookId);
    }

    @Override
    public int countByBookTitle(String title) {
        return commentRepository.countByBookTitle(title);
    }
}
