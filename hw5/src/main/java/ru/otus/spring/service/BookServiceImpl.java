package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book insert(String title, long authorId, List<Long> genresIds) {
        return save(0, title, authorId, genresIds);
    }

    public Book update(long id, String title, long authorId, List<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    public int countByAuthorId(long authorId) {
        return bookRepository.countByAuthorId(authorId);
    }

    public int countByAuthorFullName(String authorFullName) {
        return bookRepository.countByAuthorFullName(authorFullName);
    }

    public int countByGenreId(long genreId) {
        return bookRepository.countByGenreId(genreId);
    }

    public int countByGenreName(String genreName) {
        return bookRepository.countByGenreName(genreName);
    }

    private Book save(long id, String title, long authorId, List<Long> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
