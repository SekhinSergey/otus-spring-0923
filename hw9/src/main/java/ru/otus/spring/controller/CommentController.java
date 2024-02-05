package ru.otus.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.mapper.CommentMapper;
import ru.otus.spring.model.Comment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("all")
public class CommentController {

    private final CommentService commentService;

    private final BookService bookService;

    private final CommentMapper commentMapper;

    @GetMapping("/comments")
    public String getAllByBookId(@RequestParam("bookId") long bookId, Model model) {
        List<CommentDto> comments = commentService.findAllByBookId(bookId).stream()
                .map(commentMapper::mapEntityToDto)
                .toList();
        model.addAttribute("comments", comments);
        return "comments";
    }

    @GetMapping("/editComment")
    public String getCommentForEditing(@RequestParam("id") long id, Model model) {
        Comment comment = commentService.findById(id);
        model.addAttribute("comment", CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build());
        return "editComment";
    }

    @PostMapping("/editComment")
    public String editComment(@Valid @ModelAttribute("commentDto") CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/editComment?id=" + commentDto.getId();
        }
        commentService.update(Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .book(bookService.findById(commentDto.getBookId()))
                .build());
        return "redirect:/";
    }

    @GetMapping("/addComment")
    public String formEmptyComment(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("comment", CommentDto.builder()
                .bookId(bookId)
                .build());
        return "addComment";
    }

    @PostMapping("/addComment")
    public String addComment(@Valid @ModelAttribute("commentDto") CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/addComment?bookId=" + commentDto.getBookId();
        }
        commentService.create(Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .book(bookService.findById(commentDto.getBookId()))
                .build());
        return "redirect:/";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("id") long id) {
        commentService.deleteById(id);
        return "redirect:/";
    }
}
