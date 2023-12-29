package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.model.Comment;
import ru.otus.spring.service.CommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all comments", key = "ac")
    public String findAllComments() {
        return commentService.findAll().stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cbyid")
    public String findCommentById(long id) {
        return commentConverter.commentToString(commentService.findById(id));
    }

    @ShellMethod(value = "Find comment by text", key = "cbyt")
    public String findCommentByText(String text) {
        return commentConverter.commentToString(commentService.findByText(text));
    }

    @ShellMethod(value = "Find all comments by book id", key = "acbybid")
    public String findAllCommentsByBookId(long bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all comments by book title", key = "acbybt")
    public String findAllCommentsByBookTitle(String title) {
        return commentService.findAllByBookTitle(title).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Save comment", key = "insc")
    public String saveComment(Comment comment) {
        return commentConverter.commentToString(commentService.save(comment));
    }

    @ShellMethod(value = "Delete all comments by book id", key = "delacbybid")
    public String deleteAllCommentsByBookId(long bookId) {
        commentService.deleteAllByBookId(bookId);
        return "Comments with book id %d deleted successfully".formatted(bookId);
    }

    @ShellMethod(value = "Delete all comments by book title", key = "delacbybt")
    public String deleteAllCommentsByBookTitle(String title) {
        commentService.deleteAllByBookTitle(title);
        return "Comments with book title %s deleted successfully".formatted(title);
    }

    @ShellMethod(value = "Count comments by book id", key = "ccbybid")
    public int countCommentsByBookId(long bookId) {
        return commentService.countByBookId(bookId);
    }

    @ShellMethod(value = "Count comments by book title", key = "ccbybt")
    public int countCommentsByBookTitle(String title) {
        return commentService.countByBookTitle(title);
    }
}
