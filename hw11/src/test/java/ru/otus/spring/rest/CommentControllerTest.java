package ru.otus.spring.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.response.ErrorDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.utils.IntegrationTest;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.EntityUtils.FIRST_ID;
import static ru.otus.spring.utils.EntityUtils.FOURTH_ID;
import static ru.otus.spring.utils.EntityUtils.THIRD_COMMENT;
import static ru.otus.spring.utils.EntityUtils.THIRD_ID;
import static ru.otus.spring.utils.EntityUtils.getFirstBook;
import static ru.otus.spring.utils.EntityUtils.getThirdComment;
import static ru.otus.spring.utils.Messages.NO_BOOK_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_COMMENT_TEXT_ERROR_MESSAGE;
import static ru.otus.spring.utils.TestMessages.NON_EXISTENT_BOOK_ID;
import static ru.otus.spring.utils.TestMessages.NON_EXISTENT_BOOK_NOT_FOUND_ERROR_MESSAGE;

class CommentControllerTest extends IntegrationTest {

    private static final String NON_EXISTENT_COMMENT_ID = "4";

    private static final String NEW_COMMENT_TEXT = "Comment_4";

    private static final String NON_EXISTENT_COMMENT_NOT_FOUND_ERROR_MESSAGE = "Comment with id 4 not found";

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    void shouldGetAllCommentsByBookId() {
        when(commentRepository.findAllByBookId(anyString())).thenReturn(Flux.just(getThirdComment()));
        webTestClientBuild
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/library/comments")
                        .queryParam("bookId", THIRD_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(1)
                .contains(CommentDto.builder()
                        .id(THIRD_ID)
                        .text(THIRD_COMMENT)
                        .bookId(THIRD_ID)
                        .build());
    }

    @ParameterizedTest()
    @MethodSource("getValidationErrorInfo")
    void shouldReturnUpdateDtoValidationErrorMessages(String text, String bookId) {
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment/{id}").build(NON_EXISTENT_COMMENT_ID))
                .body(BodyInserters.fromValue(CommentUpdateDto.builder()
                        .text(text)
                        .bookId(bookId)
                        .build()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDto.class)
                .value(errorDto -> {
                    var errors = errorDto.errors().stream().toList();
                    assertEquals(NO_BOOK_ID_ERROR_MESSAGE, errors.get(0).message());
                    assertEquals(NO_COMMENT_TEXT_ERROR_MESSAGE, errors.get(1).message());
                });
    }

    @ParameterizedTest()
    @MethodSource("getValidationErrorInfo")
    void shouldReturnCreateDtoValidationErrorMessages(String text, String bookId) {
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment").build())
                .body(BodyInserters.fromValue(CommentCreateDto.builder()
                        .text(text)
                        .bookId(bookId)
                        .build()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDto.class)
                .value(errorDto -> {
                    var errors = errorDto.errors().stream().toList();
                    assertEquals(NO_BOOK_ID_ERROR_MESSAGE, errors.get(0).message());
                    assertEquals(NO_COMMENT_TEXT_ERROR_MESSAGE, errors.get(1).message());
                });
    }

    private static Stream<Arguments> getValidationErrorInfo() {
        return Stream.of(Arguments.of(null, null), Arguments.of(EMPTY, EMPTY));
    }

    @Test
    void shouldTryUpdateAndReturnNoCommentErrorMessage() {
        when(commentRepository.findById(anyString())).thenReturn(Mono.empty());
        when(bookRepository.findById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment/{id}").build(NON_EXISTENT_COMMENT_ID))
                .body(BodyInserters.fromValue(CommentUpdateDto.builder()
                        .text(NEW_COMMENT_TEXT)
                        .bookId(NON_EXISTENT_BOOK_ID)
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> {
                    assertEquals(NON_EXISTENT_COMMENT_NOT_FOUND_ERROR_MESSAGE, error);
                });
    }

    @Test
    void shouldTryUpdateAndReturnNoBookErrorMessage() {
        when(commentRepository.findById(anyString())).thenReturn(Mono.just(getThirdComment()));
        when(bookRepository.findById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment/{id}").build(THIRD_ID))
                .body(BodyInserters.fromValue(CommentUpdateDto.builder()
                        .text(NEW_COMMENT_TEXT)
                        .bookId(NON_EXISTENT_BOOK_ID)
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> {
                    assertEquals(NON_EXISTENT_BOOK_NOT_FOUND_ERROR_MESSAGE, error);
                });
    }

    @Test
    void shouldUpdate() {
        when(commentRepository.findById(anyString())).thenReturn(Mono.just(getThirdComment()));
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getFirstBook()));
        when(commentRepository.save(any()))
                .thenReturn(Mono.just(Comment.builder()
                        .id(THIRD_ID)
                        .text(NEW_COMMENT_TEXT)
                        .book(getFirstBook())
                        .build()));
        webTestClientBuild
                .put()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment/{id}").build(THIRD_ID))
                .body(BodyInserters.fromValue(CommentUpdateDto.builder()
                        .text(NEW_COMMENT_TEXT)
                        .bookId(FIRST_ID)
                        .build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentDto.class)
                .value(bookDto -> assertEquals(
                        CommentDto.builder()
                                .id(THIRD_ID)
                                .text(NEW_COMMENT_TEXT)
                                .bookId(FIRST_ID)
                                .build(),
                        bookDto));
    }

    @Test
    void shouldTryCreateAndReturnNoBookErrorMessage() {
        when(commentRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.empty());
        when(bookRepository.findById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment").build())
                .body(BodyInserters.fromValue(CommentCreateDto.builder()
                        .text(NEW_COMMENT_TEXT)
                        .bookId(NON_EXISTENT_BOOK_ID)
                        .build()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> {
                    assertEquals(NON_EXISTENT_BOOK_NOT_FOUND_ERROR_MESSAGE, error);
                });
    }

    @Test
    void shouldCreateFirstly() {
        when(commentRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.empty());
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getFirstBook()));
        when(commentRepository.save(any()))
                .thenReturn(Mono.just(Comment.builder()
                        .id(FIRST_ID)
                        .text(NEW_COMMENT_TEXT)
                        .book(getFirstBook())
                        .build()));
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment").build())
                .body(BodyInserters.fromValue(CommentUpdateDto.builder()
                        .text(NEW_COMMENT_TEXT)
                        .bookId(FIRST_ID)
                        .build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentDto.class)
                .value(bookDto -> assertEquals(
                        CommentDto.builder()
                                .id(FIRST_ID)
                                .text(NEW_COMMENT_TEXT)
                                .bookId(FIRST_ID)
                                .build(),
                        bookDto));
    }

    @Test
    void shouldCreate() {
        when(commentRepository.findFirstByOrderByIdDesc()).thenReturn(Mono.just(getThirdComment()));
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(getFirstBook()));
        when(commentRepository.save(any()))
                .thenReturn(Mono.just(Comment.builder()
                        .id(FOURTH_ID)
                        .text(NEW_COMMENT_TEXT)
                        .book(getFirstBook())
                        .build()));
        webTestClientBuild
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/library/comment").build())
                .body(BodyInserters.fromValue(CommentUpdateDto.builder()
                        .text(NEW_COMMENT_TEXT)
                        .bookId(FIRST_ID)
                        .build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentDto.class)
                .value(bookDto -> assertEquals(
                        CommentDto.builder()
                                .id(FOURTH_ID)
                                .text(NEW_COMMENT_TEXT)
                                .bookId(FIRST_ID)
                                .build(),
                        bookDto));
    }

    @Test
    void shouldTryDeleteAndReturnNoCommentError() {
        when(commentRepository.findById(anyString())).thenReturn(Mono.empty());
        when(commentRepository.deleteById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/library/comment")
                        .queryParam("id", NON_EXISTENT_BOOK_ID)
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(error -> assertEquals(NON_EXISTENT_COMMENT_NOT_FOUND_ERROR_MESSAGE, error));
    }

    @Test
    void shouldDelete() {
        when(commentRepository.findById(anyString())).thenReturn(Mono.just(getThirdComment()));
        when(commentRepository.deleteById(anyString())).thenReturn(Mono.empty());
        webTestClientBuild
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/library/comment")
                        .queryParam("id", THIRD_ID)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
        when(commentRepository.findAllByBookId(anyString())).thenReturn(Flux.empty());
        webTestClientBuild
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/library/comments")
                        .queryParam("bookId", THIRD_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(0);
    }
}
