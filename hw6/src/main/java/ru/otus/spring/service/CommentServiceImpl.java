package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findByText(String text) {
        return commentRepository.findByText(text)
                .orElseThrow(() -> new EntityNotFoundException("Comment with text %s not found".formatted(text)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(long bookId) {
        List<Comment> comments = commentRepository.findAllByBookId(bookId);
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("Comments with book id %d not found".formatted(bookId));
        }
        return comments;
    }

    @Override
    @Transactional(readOnly = true)
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
        Long bookId = comment.getBook().getId();
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteAllByBookId(long bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteAllByBookTitle(String title) {
        commentRepository.deleteAllByBookTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByBookId(long bookId) {
        return commentRepository.countByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByBookTitle(String title) {
        return commentRepository.countByBookTitle(title);
    }
}
