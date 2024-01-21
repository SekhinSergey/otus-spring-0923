package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    @SuppressWarnings("all")
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> book.getGenres().size());
        return books;
    }

    @Override
    @Transactional
    public Book insert(String title, long authorId, Set<Long> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public Book update(Long id, String title, long authorId, Set<Long> genresIds) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return save(id, title, authorId, genresIds);
    }

    private Book save(Long id, String title, long authorId, Set<Long> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        List<Genre> genres = getGenres(genresIds);
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }

    private List<Genre> getGenres(Set<Long> genresIds) {
        if (genresIds.isEmpty()) {
            throw new EntityNotFoundException("Requested genre list is empty");
        }
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (genresIds.size() != genres.size()) {
            throw new EntityNotFoundException(
                    "The number of requested genres does not match the number in the database");
        }
        return genres;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
        commentRepository.deleteAllByBookId(id);
    }

    @Override
    @Transactional
    public void deleteByTitle(String title) {
        bookRepository.deleteByTitle(title);
        commentRepository.deleteAllByBookTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByAuthorId(long authorId) {
        return bookRepository.countByAuthorId(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByAuthorFullName(String authorFullName) {
        return bookRepository.countByAuthorFullName(authorFullName);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByGenreId(long genreId) {
        return bookRepository.countByGenresId(genreId);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByGenreName(String genreName) {
        return bookRepository.countByGenresName(genreName);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findByExample(Book book) {
        return bookRepository.findOne(Example.of(book))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with title %s not found".formatted(book.getTitle())));
    }

    @Override
    @Transactional
    public List<Book> saveBatch(Set<Book> books) {
        return bookRepository.saveAll(books);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Set<Long> ids) {
        bookRepository.deleteAllByIdInBatch(ids);
        commentRepository.deleteAllByBookIdIn(ids);
    }
}
