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
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.Comparator;

import static ru.otus.spring.utils.Messages.NO_BOOK_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_COMMENT_BY_ID_ERROR_MESSAGE;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @GetMapping("/api/library/comments")
    public Flux<CommentDto> getAllByBookId(@RequestParam("bookId") String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .map(this::toDto)
                .sort(Comparator.comparing(CommentDto::id));
    }

    @PutMapping("/api/library/comment/{id}")
    public Mono<ResponseEntity<CommentDto>> edit(@PathVariable String id,
                                                 @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(NO_COMMENT_BY_ID_ERROR_MESSAGE.formatted(id))))
                .zipWith(bookRepository.findById(commentUpdateDto.getBookId())
                        .switchIfEmpty(Mono.error(new NotFoundException(
                                NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(commentUpdateDto.getBookId())))))
                .flatMap(data -> Mono.just(Comment.builder()
                        .id(id)
                        .text(commentUpdateDto.getText())
                        .book(data.getT2())
                        .build()))
                .flatMap(commentRepository::save)
                .map(this::toDto)
                .map(commentDto -> ResponseEntity.status(HttpStatus.CREATED).body(commentDto));
    }

    @PostMapping("/api/library/comment")
    public Mono<ResponseEntity<CommentDto>> add(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        Mono<Integer> lastDbId = commentRepository.findFirstByOrderByIdDesc()
                .switchIfEmpty(Mono.just(Comment.builder()
                        .id("0")
                        .build()))
                .map(Comment::getId)
                .map(Integer::parseInt);
        String bookId = commentCreateDto.getBookId();
        Mono<Book> dbBook = bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(bookId))));
        return Mono
                .zip(lastDbId, dbBook)
                .flatMap(data -> Mono.just(Comment.builder()
                        .id(String.valueOf(data.getT1() + 1))
                        .text(commentCreateDto.getText())
                        .book(data.getT2())
                        .build()))
                .flatMap(commentRepository::save)
                .map(this::toDto)
                .map(commentDto -> ResponseEntity.status(HttpStatus.CREATED).body(commentDto));
    }

    private CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build();
    }

    @DeleteMapping("/api/library/comment")
    public Mono<ResponseEntity<Void>> delete(@RequestParam("id") String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(NO_COMMENT_BY_ID_ERROR_MESSAGE.formatted(id))))
                .zipWith(commentRepository.deleteById(id))
                .thenReturn(ResponseEntity.noContent().build());
    }
}
