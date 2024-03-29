package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.BookDto;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;
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
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public BookUpdateDto findByIdForEditing(Long id) {
        return bookMapper.toUpdateDto(bookRepository.findById(id)
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
    public BookCreateDto create(BookCreateDto bookCreateDto) {
        long id = bookCreateDto.getId();
        if (bookRepository.findById(id).isPresent()) {
            throw new NotFoundException("Book with id %d already exists".formatted(id));
        }
        long authorId = bookCreateDto.getAuthorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
        Set<Long> genreIds = Arrays.stream(bookCreateDto.getGenreIds().split(SPACE))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        return bookMapper.toCreateDto(bookRepository.save(bookMapper.createDtoToEntity(bookCreateDto, author, genres)));
    }

    @Override
    @Transactional
    public BookUpdateDto update(BookUpdateDto bookUpdateDto) {
        long id = bookUpdateDto.getId();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id)));
        Long authorId = bookUpdateDto.getAuthorId();
        if (!authorId.equals(book.getAuthor().getId())) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
            book.setAuthor(author);
        }
        Set<Long> genreIds = Arrays.stream(bookUpdateDto.getGenreIds().split(SPACE))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        book.setGenres(genres);
        return bookMapper.toUpdateDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public List<BookUpdateDto> updateBatch(Set<BookUpdateDto> bookUpdateDtos) {
        checkExistence(bookUpdateDtos);
        Map<Long, Author> authorByAuthorIdMap = getAuthorByAuthorIdMap(bookUpdateDtos);
        Map<Long, Set<Long>> genreIdsByIdMap = new HashMap<>();
        Set<Long> genreIds = new HashSet<>();
        bookUpdateDtos.forEach(dto -> {
            Set<Long> bookGenreIds = Arrays.stream(dto.getGenreIds().split(SPACE))
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            genreIdsByIdMap.put(dto.getId(), bookGenreIds);
            genreIds.addAll(bookGenreIds);
        });
        Map<Long, Genre> genreByGenreIdMap = getGenreByGenreIdMap(genreIds);
        Set<Book> books = getFormedBooks(bookUpdateDtos, authorByAuthorIdMap, genreIdsByIdMap, genreByGenreIdMap);
        return bookRepository.saveAll(books).stream()
                .map(bookMapper::toUpdateDto)
                .toList();
    }

    private void checkExistence(Set<BookUpdateDto> bookUpdateDtos) {
        Set<Long> ids = bookUpdateDtos.stream()
                .map(BookUpdateDto::getId)
                .collect(Collectors.toSet());
        if (ids.size() != bookRepository.findAllById(ids).size()) {
            throw new NotFoundException(BOOKS_SIZE_ERROR_MESSAGE);
        }
    }

    private Map<Long, Author> getAuthorByAuthorIdMap(Set<BookUpdateDto> bookUpdateDtos) {
        Set<Long> authorIds = bookUpdateDtos.stream()
                .map(BookUpdateDto::getAuthorId)
                .collect(Collectors.toSet());
        Map<Long, Author> authorByAuthorIdMap = authorRepository.findAllById(authorIds).stream()
                .collect(Collectors.toMap(Author::getId, author -> author, (a, b) -> b));
        if (authorIds.size() != authorByAuthorIdMap.size()) {
            throw new NotFoundException(AUTHORS_SIZE_ERROR_MESSAGE);
        }
        return authorByAuthorIdMap;
    }

    private Map<Long, Genre> getGenreByGenreIdMap(Set<Long> genreIds) {
        Map<Long, Genre> genreByGenreIdMap = genreRepository.findAllById(genreIds).stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre, (a, b) -> b));
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
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toSet());
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
    public void deleteAllByIds(Set<Long> ids) {
        commentRepository.deleteAllByBookIdIn(ids);
        bookRepository.deleteAllById(ids);
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
