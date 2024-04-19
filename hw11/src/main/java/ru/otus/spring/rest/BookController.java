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
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static ru.otus.spring.utils.Messages.GENRES_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_AUTHOR_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_BOOK_BY_ID_ERROR_MESSAGE;

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
    public Mono<ResponseEntity<BookDto>> edit(@PathVariable String id,
                                              @Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id))))
                .zipWith(getAuthor(bookUpdateDto.getAuthorId()))
                .zipWith(genreRepository.findAllById(bookUpdateDto.getGenreIds()).collectList())
                .flatMap(data -> Mono.just(Book.builder()
                        .id(id)
                        .title(bookUpdateDto.getTitle())
                        .author(data.getT1().getT2())
                        .genres(bookUpdateDto.getGenreIds().size() == new HashSet<>(data.getT2()).size()
                                ? new HashSet<>(data.getT2())
                                : Set.of())
                        .build()))
                .filter(book -> !book.getGenres().isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException(GENRES_SIZE_ERROR_MESSAGE)))
                .flatMap(bookRepository::save)
                .map(this::toDto)
                .map(bookDto -> ResponseEntity.status(HttpStatus.CREATED).body(bookDto));
    }

    @PostMapping("/api/library/book")
    public Mono<ResponseEntity<BookDto>> add(@Valid @RequestBody BookCreateDto bookCreateDto) {
        Mono<Integer> lastDbId = bookRepository.findFirstByOrderByIdDesc()
                .switchIfEmpty(Mono.just(Book.builder()
                        .id("0")
                        .build()))
                .map(Book::getId)
                .map(Integer::parseInt);
        Mono<List<Genre>> dbGenres = genreRepository.findAllById(bookCreateDto.getGenreIds()).collectList();
        return Mono
                .zip(lastDbId, getAuthor(bookCreateDto.getAuthorId()), dbGenres)
                .flatMap(data -> Mono.just(Book.builder()
                        .id(String.valueOf(data.getT1() + 1))
                        .title(bookCreateDto.getTitle())
                        .author(data.getT2())
                        .genres(bookCreateDto.getGenreIds().size() == new HashSet<>(data.getT3()).size()
                                ? new HashSet<>(data.getT3())
                                : Set.of())
                        .build()))
                .filter(book -> !book.getGenres().isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException(GENRES_SIZE_ERROR_MESSAGE)))
                .flatMap(bookRepository::save)
                .map(this::toDto)
                .map(bookDto -> ResponseEntity.status(HttpStatus.CREATED).body(bookDto));
    }

    public Mono<Author> getAuthor(String authorId) {
        return authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(authorId))));
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
    public Mono<ResponseEntity<Void>> delete(@RequestParam("id") String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(id))))
                .zipWith(commentRepository.deleteAllByBookId(id))
                .zipWith(bookRepository.deleteById(id))
                .thenReturn(ResponseEntity.noContent().build());
    }
}
