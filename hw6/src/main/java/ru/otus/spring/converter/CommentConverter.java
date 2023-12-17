package ru.otus.spring.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.model.Comment;

@Component
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "Id: %d, Text: %s".formatted(comment.getId(), comment.getText());
    }
}
