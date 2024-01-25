package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String OR_ELSE_THROW_RULE = "java:S2201";

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookUtil bookUtil;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @SuppressWarnings("all")
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book create(Book book) {
        return save(book);
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public Book update(Book book) {
        String id = book.getId();
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        return save(book);
    }

    private Book save(Book book) {
        bookUtil.validateBook(book);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
        commentRepository.deleteAllByBookId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByAuthorId(String authorId) {
        return bookRepository.countByAuthorId(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByGenreId(String genreId) {
        return bookRepository.countByGenresId(genreId);
    }

    @Override
    @Transactional
    public List<Book> createBatch(Set<Book> books) {
        books.forEach(bookUtil::validateBook);
        return bookRepository.saveAll(books);
    }

    @Override
    @Transactional
    @SuppressWarnings(OR_ELSE_THROW_RULE)
    public List<Book> updateBatch(Set<Book> books) {
        books.forEach(book -> {
            String id = book.getId();
            bookRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
            bookUtil.validateBook(book);
        });
        return bookRepository.saveAll(books);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<String> ids) {
        bookRepository.deleteAllById(ids);
        commentRepository.deleteAllByBookIdIn(ids);
    }
}
