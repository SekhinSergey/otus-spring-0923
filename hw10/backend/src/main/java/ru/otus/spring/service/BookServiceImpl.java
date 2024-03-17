package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static ru.otus.spring.constant.Constants.AUTHORS_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.BOOKS_SIZE_ERROR_MESSAGE;
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

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> book.getGenres().size());
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAllByIds(Set<Long> ids) {
        return bookRepository.findAllById(ids).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto create(BookCreateDto bookCreateDto) {
        long authorId = bookCreateDto.getAuthorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
        Set<Long> genreIds = bookCreateDto.getGenreIds();
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        return bookMapper.toDto(bookRepository.save(bookMapper.createDtoToEntity(bookCreateDto, author, genres)));
    }

    @Override
    @Transactional
    public BookDto update(long id, BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id)));
        String title = bookUpdateDto.getTitle();
        if (!Objects.equals(title, book.getTitle())) {
            book.setTitle(title);
        }
        Long authorId = bookUpdateDto.getAuthorId();
        if (!Objects.equals(authorId, book.getAuthor().getId())) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
            book.setAuthor(author);
        }
        Set<Long> genreIds = bookUpdateDto.getGenreIds();
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        book.setGenres(genres);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public List<BookDto> updateBatch(Set<BookUpdateDto> bookUpdateDtos) {
        checkExistence(bookUpdateDtos);
        Map<Long, Author> authorByAuthorIdMap = getAuthorByAuthorIdMap(bookUpdateDtos);
        Map<Long, Set<Long>> genreIdsByIdMap = new HashMap<>();
        Set<Long> genreIds = new HashSet<>();
        bookUpdateDtos.forEach(dto -> {
            Set<Long> bookGenreIds = dto.getGenreIds();
            genreIdsByIdMap.put(dto.getId(), bookGenreIds);
            genreIds.addAll(bookGenreIds);
        });
        Map<Long, Genre> genreByGenreIdMap = getGenreByGenreIdMap(genreIds);
        Set<Book> books = getFormedBooks(bookUpdateDtos, authorByAuthorIdMap, genreIdsByIdMap, genreByGenreIdMap);
        return bookRepository.saveAll(books).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    private void checkExistence(Set<BookUpdateDto> bookUpdateDtos) {
        Set<Long> ids = bookUpdateDtos.stream()
                .map(BookUpdateDto::getId)
                .collect(toSet());
        if (ids.size() != bookRepository.findAllById(ids).size()) {
            throw new NotFoundException(BOOKS_SIZE_ERROR_MESSAGE);
        }
    }

    private Map<Long, Author> getAuthorByAuthorIdMap(Set<BookUpdateDto> bookUpdateDtos) {
        Set<Long> authorIds = bookUpdateDtos.stream()
                .map(BookUpdateDto::getAuthorId)
                .collect(toSet());
        Map<Long, Author> authorByAuthorIdMap = authorRepository.findAllById(authorIds).stream()
                .collect(toMap(Author::getId, author -> author, (a, b) -> b));
        if (authorIds.size() != authorByAuthorIdMap.size()) {
            throw new NotFoundException(AUTHORS_SIZE_ERROR_MESSAGE);
        }
        return authorByAuthorIdMap;
    }

    private Map<Long, Genre> getGenreByGenreIdMap(Set<Long> genreIds) {
        Map<Long, Genre> genreByGenreIdMap = genreRepository.findAllById(genreIds).stream()
                .collect(toMap(Genre::getId, genre -> genre, (a, b) -> b));
        if (genreIds.size() != genreByGenreIdMap.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        return genreByGenreIdMap;
    }

    private static Set<Book> getFormedBooks(Set<BookUpdateDto> bookUpdateDtos,
                                            Map<Long, Author> authorByAuthorIdMap,
                                            Map<Long, Set<Long>> genreIdsByIdMap,
                                            Map<Long, Genre> genreByGenreIdMap) {
        return bookUpdateDtos.stream().map(dto -> Book.builder()
                        .id(dto.getId())
                        .title(dto.getTitle())
                        .author(authorByAuthorIdMap.get(dto.getAuthorId()))
                        .genres(genreIdsByIdMap.get(dto.getId()).stream()
                                .map(genreByGenreIdMap::get)
                                .collect(toSet()))
                        .build())
                .collect(toSet());
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        findById(id);
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
    public void deleteAllByIds(Set<Long> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
        bookRepository.deleteAllById(ids);
    }
}
