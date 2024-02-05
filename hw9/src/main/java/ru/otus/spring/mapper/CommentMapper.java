package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.model.Comment;

@Component
public class CommentMapper {

    public CommentDto mapEntityToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build();
    }
}
