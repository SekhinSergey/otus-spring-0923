package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String NOT_FOUND_MESSAGE = "Book with id %d not found";

    private static final String OR_ELSE_THROW_RULE = "java:S2201";

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookValidator bookValidator;

    @Override
    @SuppressWarnings("all")
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> book.getGenres().size());
        return books;
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE.formatted(id)));
    }

    @Override
    public List<Book> findAllByIds(Set<Long> ids) {
        return bookRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public Book create(Book book) {
        throwExceptionIfExists(book);
        return save(book);
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public Book update(Book book) {
        Long id = book.getId();
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE.formatted(id)));
        return save(book);
    }

    private Book save(Book book) {
        bookValidator.validateBook(book);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteAllByBookId(id);
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByAuthorId(long authorId) {
        return bookRepository.countByAuthorId(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByGenreId(long genreId) {
        return bookRepository.countByGenresId(genreId);
    }

    @Override
    @Transactional
    public List<Book> createBatch(Set<Book> books) {
        books.forEach(book -> {
            throwExceptionIfExists(book);
            bookValidator.validateBook(book);
        });
        return bookRepository.saveAll(books);
    }

    private void throwExceptionIfExists(Book book) {
        Long id = book.getId();
        if (bookRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Book with id %d already exists".formatted(id));
        }
    }

    @Override
    @Transactional
    @SuppressWarnings(OR_ELSE_THROW_RULE)
    public List<Book> updateBatch(Set<Book> books) {
        books.forEach(book -> {
            Long id = book.getId();
            bookRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE.formatted(id)));
            bookValidator.validateBook(book);
        });
        return bookRepository.saveAll(books);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<Long> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
        bookRepository.deleteAllById(ids);
    }
}
