package ru.otus.spring.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.response.ErrorDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.model.Book;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.utils.IntegrationTest;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.EntityUtils.FIFTH_ID;
import static ru.otus.spring.utils.EntityUtils.FIRST_BOOK_TITLE;
import static ru.otus.spring.utils.EntityUtils.FIRST_ID;
import static ru.otus.spring.utils.EntityUtils.FOURTH_ID;
import static ru.otus.spring.utils.EntityUtils.SECOND_BOOK_TITLE;
import static ru.otus.spring.utils.EntityUtils.SECOND_ID;
import static ru.otus.spring.utils.EntityUtils.SIXTH_ID;
import static ru.otus.spring.utils.EntityUtils.THIRD_BOOK_TITLE;
import static ru.otus.spring.utils.EntityUtils.THIRD_ID;
import static ru.otus.spring.utils.EntityUtils.getFirstAuthor;
import static ru.otus.spring.utils.EntityUtils.getFirstBook;
import static ru.otus.spring.utils.EntityUtils.getFirstGenre;
import static ru.otus.spring.utils.EntityUtils.getSecondBook;
import static ru.otus.spring.utils.EntityUtils.getThirdBook;
import static ru.otus.spring.utils.Messages.GENRES_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_AUTHOR_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_BOOK_TITLE_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_GENRE_IDS_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_GENRE_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.TestMessages.NON_EXISTENT_BOOK_ID;
import static ru.otus.spring.utils.TestMessages.NON_EXISTENT_BOOK_NOT_FOUND_ERROR_MESSAGE;

class BookControllerTest extends IntegrationTest {

    private static final String NON_EXISTENT_AUTHOR_ID = "4";

    private static final String NEW_BOOK_TITLE = "BookTitle_4";

    private static final String NON_EXISTENT_AUTHOR_NOT_FOUND_ERROR_MESSAGE = "Author with id 4 not found";

    private static final String NON_EXISTENT_GENRE_ID = "7";

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    void shouldGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Flux.just(getFirstBook(), getSecondBook(), getThirdBook()));
        webTestClientBuild
                .get()
                .uri("/api/library/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(3)
                .contains(
                        BookDto.builder()
                                .id(FIRST_ID)
                                .title(FIRST_BOOK_TITLE)
                                .authorId(FIRST_ID)
                                .genreIds(Set.of(FIRST_ID, SECOND_ID))
                                .build(),
                        BookDto.builder()
                                .id(SECOND_ID)
                                .title(SECOND_BOOK_TITLE)
                                .authorId(SECOND_ID)
                                .genreIds(Set.of(THIRD_ID, FOURTH_ID))
                                .build(),
                        BookDto.builder()
                                .id(THIRD_ID)
                                .title(THIRD_BOOK_TITLE)
                                .authorId(THIRD_ID)
                                .genreIds(Set.of(FIFTH_ID, SIXTH_ID))
                                .build());
    }

    @ParameterizedTest()
    @MethodSource("getValidationErrorInfo")
    void shouldReturnUpdateDtoValidationErrorMessages(String title,
                                                      String authorId,
                                                      Set<String> genreIds,
                                                      String genreErrorMessage) {
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book/{id}").build(NON_EXISTENT_BOOK_ID))
                .body(BodyInserters.fromValue(BookUpdateDto.builder()
                        .title(title)
                        .authorId(authorId)
                        .genreIds(genreIds)
                        .build()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDto.class)
                .value(errorDto -> {
                    var errors = errorDto.errors().stream().toList();
                    assertEquals(genreErrorMessage, errors.get(0).message());
                    assertEquals(NO_BOOK_TITLE_ERROR_MESSAGE, errors.get(1).message());
                    assertEquals(NO_AUTHOR_ID_ERROR_MESSAGE, errors.get(2).message());
                });
    }

    @ParameterizedTest()
    @MethodSource("getValidationErrorInfo")
    void shouldReturnCreateDtoValidationErrorMessages(String title,
                                                      String authorId,
                                                      Set<String> genreIds,
                                                      String genreErrorMessage) {
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book").build())
                .body(BodyInserters.fromValue(BookCreateDto.builder()
                        .title(title)
                        .authorId(authorId)
                        .genreIds(genreIds)
                        .build()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDto.class)
                .value(errorDto -> {
                    Map<String, String> errorMap = errorDto.errors().stream()
                            .collect(
                                    Collectors.toMap(ErrorDto.Error::field, ErrorDto.Error::message, (a, b) -> b,
                                    TreeMap::new));
                    assertEquals(NO_AUTHOR_ID_ERROR_MESSAGE, errorMap.get("authorId"));
                    assertEquals(NO_BOOK_TITLE_ERROR_MESSAGE, errorMap.get("title"));
                    assertEquals(
                            genreErrorMessage,
                            errorMap.get("genreIds") == null ? errorMap.get("genreIds[]") : errorMap.get("genreIds"));
                });
    }

    private static Stream<Arguments> getValidationErrorInfo() {
        return Stream.of(
                Arguments.of(null, null, null, NO_GENRE_IDS_ERROR_MESSAGE),
                Arguments.of(EMPTY, EMPTY, Set.of(), NO_GENRE_IDS_ERROR_MESSAGE),
                Arguments.of(EMPTY, EMPTY, Set.of(""), NO_GENRE_ID_ERROR_MESSAGE)
        );
    }

    @Test
    void shouldTryUpdateAndReturnNoBookError() {
        when(bookRepository.findById(anyString())).thenReturn(Mono.empty());
        when(authorRepository.findById(anyString())).thenReturn(Mono.empty());
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.empty());
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book/{id}").build(NON_EXISTENT_BOOK_ID))
                .body(BodyInserters.fromValue(BookUpdateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(NON_EXISTENT_AUTHOR_ID)
                        .genreIds(Set.of(NON_EXISTENT_GENRE_ID))
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(NON_EXISTENT_BOOK_NOT_FOUND_ERROR_MESSAGE, error));
    }

    @Test
    void shouldTryUpdateAndReturnNoAuthorError() {
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getThirdBook()));
        when(authorRepository.findById(anyString())).thenReturn(Mono.empty());
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.empty());
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book/{id}").build(THIRD_ID))
                .body(BodyInserters.fromValue(BookUpdateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(NON_EXISTENT_AUTHOR_ID)
                        .genreIds(Set.of(NON_EXISTENT_GENRE_ID))
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(NON_EXISTENT_AUTHOR_NOT_FOUND_ERROR_MESSAGE, error));
    }

    @Test
    void shouldTryUpdateAndReturnGenresSizeError() {
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getThirdBook()));
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getFirstAuthor()));
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.empty());
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book/{id}").build(THIRD_ID))
                .body(BodyInserters.fromValue(BookUpdateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(FIRST_ID)
                        .genreIds(Set.of(NON_EXISTENT_GENRE_ID))
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(GENRES_SIZE_ERROR_MESSAGE, error));
    }

    @Test
    void shouldUpdate() {
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getThirdBook()));
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getFirstAuthor()));
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.just(getFirstGenre()));
        when(bookRepository.save(any()))
                .thenReturn(Mono.just(Book.builder()
                        .id(THIRD_ID)
                        .title(NEW_BOOK_TITLE)
                        .author(getFirstAuthor())
                        .genres(Set.of(getFirstGenre()))
                        .build()));
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book/{id}").build(THIRD_ID))
                .body(BodyInserters.fromValue(BookUpdateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(FIRST_ID)
                        .genreIds(Set.of(FIRST_ID))
                        .build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .value(bookDto -> assertEquals(
                        BookDto.builder()
                                .id(THIRD_ID)
                                .title(NEW_BOOK_TITLE)
                                .authorId(FIRST_ID)
                                .genreIds(Set.of(FIRST_ID))
                                .build(),
                        bookDto));
    }

    @Test
    void shouldTryCreateAndReturnNoAuthorError() {
        when(bookRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.empty());
        when(authorRepository.findById(anyString())).thenReturn(Mono.empty());
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.empty());
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book").build())
                .body(BodyInserters.fromValue(BookCreateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(NON_EXISTENT_AUTHOR_ID)
                        .genreIds(Set.of(NON_EXISTENT_GENRE_ID))
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(NON_EXISTENT_AUTHOR_NOT_FOUND_ERROR_MESSAGE, error));
    }

    @Test
    void shouldTryCreateAndReturnGenresSizeError() {
        when(bookRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.empty());
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getFirstAuthor()));
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.empty());
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book").build())
                .body(BodyInserters.fromValue(BookCreateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(FIRST_ID)
                        .genreIds(Set.of(NON_EXISTENT_GENRE_ID))
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(GENRES_SIZE_ERROR_MESSAGE, error));
    }

    @Test
    void shouldCreateFirstly() {
        when(bookRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.empty());
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getFirstAuthor()));
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.just(getFirstGenre()));
        when(bookRepository.save(any()))
                .thenReturn(Mono.just(Book.builder()
                        .id(FIRST_ID)
                        .title(NEW_BOOK_TITLE)
                        .author(getFirstAuthor())
                        .genres(Set.of(getFirstGenre()))
                        .build()));
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book").build())
                .body(BodyInserters.fromValue(BookCreateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(FIRST_ID)
                        .genreIds(Set.of(FIRST_ID))
                        .build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .value(bookDto -> assertEquals(
                        BookDto.builder()
                                .id(FIRST_ID)
                                .title(NEW_BOOK_TITLE)
                                .authorId(FIRST_ID)
                                .genreIds(Set.of(FIRST_ID))
                                .build(),
                        bookDto));
    }

    @Test
    void shouldCreate() {
        when(bookRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.just(getThirdBook()));
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getFirstAuthor()));
        when(genreRepository.findAllById(anySet())).thenReturn(Flux.just(getFirstGenre()));
        when(bookRepository.save(any())).thenReturn(Mono.just(Book.builder()
                        .id(FOURTH_ID)
                        .title(NEW_BOOK_TITLE)
                        .author(getFirstAuthor())
                        .genres(Set.of(getFirstGenre()))
                        .build()));
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/book").build())
                .body(BodyInserters.fromValue(BookCreateDto.builder()
                        .title(NEW_BOOK_TITLE)
                        .authorId(FIRST_ID)
                        .genreIds(Set.of(FIRST_ID))
                        .build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .value(bookDto -> assertEquals(
                        BookDto.builder()
                                .id(FOURTH_ID)
                                .title(NEW_BOOK_TITLE)
                                .authorId(FIRST_ID)
                                .genreIds(Set.of(FIRST_ID))
                                .build(),
                        bookDto));
    }

    @Test
    void shouldTryDeleteAndReturnNoBookError() {
        when(bookRepository.findById(anyString())).thenReturn(Mono.empty());
        when(commentRepository.deleteAllByBookId(anyString())).thenReturn(Mono.empty());
        when(bookRepository.deleteById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/library/book")
                        .queryParam("id", NON_EXISTENT_BOOK_ID)
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(NON_EXISTENT_BOOK_NOT_FOUND_ERROR_MESSAGE, error));
    }

    @Test
    void shouldDelete() {
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getThirdBook()));
        when(commentRepository.deleteAllByBookId(anyString())).thenReturn(Mono.empty());
        when(bookRepository.deleteById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/library/book")
                        .queryParam("id", THIRD_ID)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
        when(bookRepository.findAll()).thenReturn(Flux.just(getFirstBook(), getSecondBook()));
        webTestClientBuild
                .get()
                .uri("/api/library/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(
                        BookDto.builder()
                                .id(FIRST_ID)
                                .title(FIRST_BOOK_TITLE)
                                .authorId(FIRST_ID)
                                .genreIds(Set.of(FIRST_ID, SECOND_ID))
                                .build(),
                        BookDto.builder()
                                .id(SECOND_ID)
                                .title(SECOND_BOOK_TITLE)
                                .authorId(SECOND_ID)
                                .genreIds(Set.of(THIRD_ID, FOURTH_ID))
                                .build());
    }
}
