package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
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

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, long authorId, Set<Long> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public Book update(Long id, String title, long authorId, Set<Long> genresIds) {
        bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return save(id, title, authorId, genresIds);
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
    public int countByAuthorId(long authorId) {
        return bookRepository.countByAuthorId(authorId);
    }

    @Override
    public int countByAuthorFullName(String authorFullName) {
        return bookRepository.countByAuthorFullName(authorFullName);
    }

    @Override
    public int countByGenreId(long genreId) {
        return bookRepository.countByGenreId(genreId);
    }

    @Override
    public int countByGenreName(String genreName) {
        return bookRepository.countByGenreName(genreName);
    }

    private Book save(Long id, String title, long authorId, Set<Long> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        List<Genre> genres = getGenres(genresIds);
        var book = new Book(id, title, author, genres);
        Book savedBook = bookRepository.save(book);
        if (isNull(savedBook)) {
            throw new EntityNotFoundException("Book with id %d not saved".formatted(id));
        }
        return savedBook;
    }

    private List<Genre> getGenres(Set<Long> genresIds) {
        if (genresIds.isEmpty()) {
            throw new EntityNotFoundException("Requested genre list is empty");
        }
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        } else if (genresIds.size() != genres.size()) {
            throw new EntityNotFoundException(
                    "The number of requested genres does not match the number in the database");
        }
        return genres;
    }
}
