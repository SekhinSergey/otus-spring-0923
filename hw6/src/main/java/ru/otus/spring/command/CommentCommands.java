package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.model.Comment;
import ru.otus.spring.service.CommentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "Find all comments by book id", key = "acbybid")
    public List<Comment> findAllByBookId(long bookId) {
        return commentService.findAllByBookId(bookId);
    }
}
