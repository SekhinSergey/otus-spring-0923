package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String OR_ELSE_THROW_RULE = "java:S2201";

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final BookUtil bookUtil;

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
    public List<Comment> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteAllByBookId(String bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByBookId(String bookId) {
        return commentRepository.countByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {
        findById(comment.getId());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public List<Comment> createBatch(Set<Comment> comments) {
        validateBooks(comments);
        return commentRepository.saveAll(comments);
    }

    @Override
    @Transactional
    public List<Comment> updateBatch(Set<Comment> comments) {
        validateComments(comments);
        return commentRepository.saveAll(comments);
    }

    private void validateComments(Set<Comment> comments) {
        Set<String> commentsIds = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toCollection(HashSet::new));
        List<Comment> foundComments = commentRepository.findAllById(commentsIds);
        if (commentsIds.size() != foundComments.size()) {
            throw new EntityNotFoundException(
                    "The number of requested comments does not match the number in the database");
        }
        validateBooks(comments);
    }

    @SuppressWarnings(OR_ELSE_THROW_RULE)
    private void validateBooks(Set<Comment> comments) {
        Set<Book> books = comments.stream()
                .map(Comment::getBook)
                .collect(Collectors.toCollection(HashSet::new));
        books.forEach(book -> {
            String id = book.getId();
            bookRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
            bookUtil.validateBook(book);
        });
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<String> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
    }
}
