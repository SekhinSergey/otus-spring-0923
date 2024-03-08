package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.service.CommentService;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/library/comments")
    public List<CommentDto> getAllByBookId(@RequestParam("bookId") long bookId) {
        return commentService.findAllByBookId(bookId);
    }

    // Надо добавить параметр, вместо валидации руками его проверять и сетить? Зачем выполнять столько лишних действий?
    @PutMapping("/api/library/comment")
    public ResponseEntity<CommentDto> edit(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.update(commentUpdateDto));
    }

    @PostMapping("/api/library/comment")
    public ResponseEntity<CommentDto> add(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(commentCreateDto));
    }

    @DeleteMapping("/api/library/comment")
    public ResponseEntity<String> delete(@RequestParam("id") long id) {
        commentService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(EMPTY);
    }
}
