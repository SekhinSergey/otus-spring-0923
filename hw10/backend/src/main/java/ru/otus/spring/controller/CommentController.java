package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/library/comments")
    public List<CommentDto> getAllByBookId(@RequestParam("bookId") long bookId) {
        return commentService.findAllByBookId(bookId);
    }

    @PutMapping("/api/library/comment")
    public CommentUpdateDto edit(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentUpdateDto);
    }

    @PostMapping("/api/library/comment")
    public CommentCreateDto add(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @DeleteMapping("/api/library/comment")
    public String delete(@RequestParam("id") long id) {
        commentService.deleteById(id);
        return "Comment with id %d deleted".formatted(id);
    }
}
