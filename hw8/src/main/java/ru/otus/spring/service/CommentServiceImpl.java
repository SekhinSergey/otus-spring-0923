package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.Set;

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
    public Comment findById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findByText(String text) {
        return commentRepository.findByText(text)
                .orElseThrow(() -> new EntityNotFoundException("Comment with text %s not found".formatted(text)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookTitle(String title) {
        return commentRepository.findAllByBookTitle(title);
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public Comment save(Comment comment) {
        String bookId = comment.getBook().getId();
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteAllByBookId(String bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteAllByBookTitle(String title) {
        commentRepository.deleteAllByBookTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByBookId(String bookId) {
        return commentRepository.countByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByBookTitle(String title) {
        return commentRepository.countByBookTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findByExample(Comment comment) {
        return commentRepository.findOne(Example.of(comment))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comment with id %s not found".formatted(comment.getId())));
    }

    @Override
    @Transactional
    public List<Comment> saveBatch(Set<Comment> comments) {
        return commentRepository.saveAll(comments);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<String> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
    }
}
