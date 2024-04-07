package ru.otus.spring.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toSet;
import static ru.otus.spring.constant.Constants.GENRES_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_AUTHOR_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_BOOK_BY_ID_ERROR_MESSAGE;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @GetMapping("/api/library/books")
    public Flux<BookDto> getAll() {
        return bookRepository.findAll()
                .map(this::toDto)
                .sort(Comparator.comparing(BookDto::id));
    }

    @PutMapping("/api/library/book/{id}")
    public ResponseEntity<Mono<BookDto>> edit(@PathVariable String id,
                                              @Valid @RequestBody BookUpdateDto bookUpdateDto) {
        Mono<String> dbId = bookRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id))))
                .map(Book::getId);
        Mono<List<Genre>> dbGenres = genreRepository.findAllById(bookUpdateDto.getGenreIds())
                .collectList();
        dbGenres.toFuture().thenApply(genres -> {
            if (bookUpdateDto.getGenreIds().size() != genres.size()) {
                throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
            }
            return genres;
        });
        String authorId = bookUpdateDto.getAuthorId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Mono.zip(dbId, getAuthor(authorId), dbGenres)
                        .flatMap(data -> Mono.just(Book.builder()
                                .id(data.getT1())
                                .title(bookUpdateDto.getTitle())
                                .author(data.getT2())
                                .genres(new HashSet<>(data.getT3()))
                                .build()))
                        .flatMap(bookRepository::save)
                        .map(this::toDto));

    }

    @PostMapping("/api/library/book")
    public ResponseEntity<Mono<BookDto>> add(@Valid @RequestBody BookCreateDto bookCreateDto) {
        Mono<Integer> lastDbId = bookRepository.findFirstByOrderByIdDesc()
                .map(Book::getId)
                .map(Integer::parseInt);
        Mono<List<Genre>> dbGenres = genreRepository.findAllById(bookCreateDto.getGenreIds())
                .collectList();
        dbGenres.toFuture().thenApply(genres -> {
            if (bookCreateDto.getGenreIds().size() != genres.size()) {
                throw new NotFoundException(GENRES_SIZE_ERROR_MESSAGE);
            }
            return genres;
        });
        String authorId = bookCreateDto.getAuthorId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Mono.zip(lastDbId, getAuthor(authorId), dbGenres)
                        .flatMap(data -> Mono.just(Book.builder()
                                .id(String.valueOf(data.getT1() + 1))
                                .title(bookCreateDto.getTitle())
                                .author(data.getT2())
                                .genres(new HashSet<>(data.getT3()))
                                .build()))
                        .flatMap(bookRepository::insert)
                        .map(this::toDto));
    }

    private Mono<Author> getAuthor(String authorId) {
        return authorRepository.findById(authorId)
                .switchIfEmpty(
                        Mono.error(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId))));
    }

    private BookDto toDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(book.getGenres().stream()
                        .map(Genre::getId)
                        .collect(toSet()))
                .build();
    }

    @DeleteMapping("/api/library/book")
    public ResponseEntity<Mono<Void>> delete(@RequestParam("id") String id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Mono.zip(commentRepository.deleteAllByBookId(id), bookRepository.deleteById(id))
                        .flatMap(data -> Mono.empty()));
    }
}
