package ru.otus.spring.controller;

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
import ru.otus.spring.dto.response.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.security.component.AccessService;
import ru.otus.spring.service.CommentService;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final AccessService accessService;

    @GetMapping("/api/library/comments")
    public List<CommentDto> getAllByBookId(@RequestParam("bookId") long bookId) {
        accessService.checkAccess();
        return commentService.findAllByBookId(bookId);
    }

    @PutMapping("/api/library/comment/{id}")
    public ResponseEntity<CommentDto> edit(@PathVariable long id,
                                           @Valid @RequestBody CommentUpdateDto dto) {
        accessService.checkAccess();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.update(id, dto));
    }

    @PostMapping("/api/library/comment")
    public ResponseEntity<CommentDto> add(@Valid @RequestBody CommentCreateDto dto) {
        accessService.checkAccess();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(dto));
    }

    @DeleteMapping("/api/library/comment")
    public ResponseEntity<String> delete(@RequestParam("id") long id) {
        accessService.checkAccess();
        commentService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(EMPTY);
    }
}
