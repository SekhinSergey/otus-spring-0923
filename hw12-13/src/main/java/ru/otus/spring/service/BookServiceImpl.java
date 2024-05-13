package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static ru.otus.spring.constant.Constants.GENRES_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_AUTHOR_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_BOOK_BY_ID_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        books.forEach(book -> book.getGenres().size());
        return books.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto create(BookCreateDto dto) {
        long authorId = dto.authorId();
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
        var genreIds = dto.genreIds();
        var genres = new HashSet<>(genreRepository.findAllById(genreIds));
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        return toDto(bookRepository.save(toEntity(dto, author, genres)));
    }

    @Override
    @Transactional
    public BookDto update(long id, BookUpdateDto dto) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id)));
        var title = dto.title();
        if (!Objects.equals(title, book.getTitle())) {
            book.setTitle(title);
        }
        var authorId = dto.authorId();
        if (!Objects.equals(authorId, book.getAuthor().getId())) {
            var author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
            book.setAuthor(author);
        }
        var genreIds = dto.genreIds();
        var genres = new HashSet<>(genreRepository.findAllById(genreIds));
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        book.setGenres(genres);
        return toDto(bookRepository.save(book));
    }

    private BookDto toDto(Book book) {
        var genreIds = book.getGenres().stream()
                .map(Genre::getId)
                .collect(toSet());
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
    }

    private Book toEntity(BookCreateDto dto, Author author, Set<Genre> genres) {
        return Book.builder()
                .title(dto.title())
                .author(author)
                .genres(genres)
                .build();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id)));
        commentRepository.deleteAllByBookId(id);
        bookRepository.deleteById(id);
    }
}
