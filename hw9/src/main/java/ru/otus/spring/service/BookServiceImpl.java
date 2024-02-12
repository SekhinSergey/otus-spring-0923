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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.otus.spring.constant.Constants.NO_AUTHOR_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_BOOK_BY_ID_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String GENRES_SIZE_ERROR_MESSAGE =
            "The number of requested genres does not match the number in the database";

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Override
    @SuppressWarnings("all")
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
        return bookMapper.toCreateDto(save(createDtoToEntity(bookCreateDto)));
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public BookUpdateDto update(BookUpdateDto bookUpdateDto) {
        long id = bookUpdateDto.getId();
        bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id)));
        return bookMapper.toUpdateDto(save(updateDtoToEntity(bookUpdateDto)));
    }

    @SuppressWarnings("all")
    private Book save(Book book) {
        long authorId = book.getAuthor().getId();
        authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
        Set<Long> genresIds = book.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toCollection(HashSet::new));
        var foundGenres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != foundGenres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
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
    public List<BookCreateDto> createBatch(Set<BookCreateDto> bookCreateDtos) {
        Set<Book> books = bookCreateDtos.stream()
                .map(this::createDtoToEntity)
                .collect(Collectors.toSet());
        return bookRepository.saveAll(books).stream()
                .map(bookMapper::toCreateDto)
                .toList();
    }

    private Book createDtoToEntity(BookCreateDto bookCreateDto) {
        Long id = bookCreateDto.getId();
        if (Objects.nonNull(id) && bookRepository.findById(id).isPresent()) {
            throw new NotFoundException("Book with id %d already exists".formatted(id));
        }
        long authorId = bookCreateDto.getAuthorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
        List<String> genreIds = Arrays.stream(bookCreateDto.getGenreIds()
                        .split(SPACE))
                        .toList();
        Set<Genre> genres = genreIds.stream()
                .map(genreId -> genreRepository.findById(Long.parseLong(genreId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        return bookMapper.createDtoToEntity(bookCreateDto, author, genres);
    }

    @Override
    @Transactional
    public List<BookUpdateDto> updateBatch(Set<BookUpdateDto> bookUpdateDtos) {
        Set<Book> books = bookUpdateDtos.stream()
                .map(this::updateDtoToEntity)
                .collect(Collectors.toSet());
        return bookRepository.saveAll(books).stream()
                .map(bookMapper::toUpdateDto)
                .toList();
    }

    private Book updateDtoToEntity(BookUpdateDto bookUpdateDto) {
        long authorId = bookUpdateDto.getAuthorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId)));
        List<String> genreIds = Arrays.stream(bookUpdateDto.getGenreIds()
                        .split(SPACE))
                        .toList();
        Set<Genre> genres = genreIds.stream()
                .map(genreId -> genreRepository.findById(Long.parseLong(genreId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        if (genreIds.size() != genres.size()) {
            throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
        }
        return bookMapper.updateDtoToEntity(bookUpdateDto, author, genres);
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
