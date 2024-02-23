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
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public String getAllByBookId(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("comments", commentService.findAllByBookId(bookId));
        return "comments";
    }

    @GetMapping("/editComment")
    public String getCommentForEditing(@RequestParam("id") long id, Model model) {
        model.addAttribute("comment", commentService.findByIdForEditing(id));
        return "editComment";
    }

    @PostMapping("/editComment")
    public String editComment(@Valid @ModelAttribute("commentDto") CommentUpdateDto commentUpdateDto,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/editComment?id=" + commentUpdateDto.getId();
        }
        commentService.update(commentUpdateDto);
        return "redirect:/";
    }

    @GetMapping("/addComment")
    public String formEmptyComment(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("comment", CommentCreateDto.builder().bookId(bookId).build());
        return "addComment";
    }

    @PostMapping("/addComment")
    public String addComment(@Valid @ModelAttribute("commentDto") CommentCreateDto commentCreateDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/addComment?bookId=" + commentCreateDto.getBookId();
        }
        commentService.create(commentCreateDto);
        return "redirect:/";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("id") long id) {
        commentService.deleteById(id);
        return "redirect:/";
    }
}
