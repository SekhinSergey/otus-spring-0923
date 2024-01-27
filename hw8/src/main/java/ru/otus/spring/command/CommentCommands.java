package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.model.Comment;
import ru.otus.spring.service.CommentService;

import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @SuppressWarnings("all")
    @ShellMethod(value = "Find all comments", key = "ac")
    public String findAllComments() {
        return commentService.findAll().stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find comment by id", key = "cbyid")
    public String findCommentById(String id) {
        return commentConverter.commentToString(commentService.findById(id));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find all comments by book id", key = "acbybid")
    public String findAllCommentsByBookId(String bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete all comments by book id", key = "delacbybid")
    public String deleteAllCommentsByBookId(String bookId) {
        commentService.deleteAllByBookId(bookId);
        return "Comments with book id %d deleted successfully".formatted(bookId);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Count comments by book id", key = "ccbybid")
    public int countCommentsByBookId(String bookId) {
        return commentService.countByBookId(bookId);
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create comment", key = "cnew")
    public String createComment(Comment comment) {
        return commentConverter.commentToString(commentService.create(comment));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(Comment comment) {
        return commentConverter.commentToString(commentService.update(comment));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create comment batch", key = "cnewb")
    public String createCommentBatch(Set<Comment> comments) {
        return commentService.createBatch(comments).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update comment batch", key = "cupdb")
    public String updateCommentBatch(Set<Comment> comments) {
        return commentService.updateBatch(comments).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Delete comments by ids", key = "cdelbyids")
    public String deleteCommentsByIds(Set<String> ids) {
        commentService.deleteAllByIds(ids);
        return "Comments deleted successfully";
    }
}
