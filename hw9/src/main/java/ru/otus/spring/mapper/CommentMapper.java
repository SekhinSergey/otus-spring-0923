package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build();
    }

    public CommentCreateDto toCreateDto(Comment comment) {
        return CommentCreateDto.builder()
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build();
    }

    public Comment createDtoToEntity(CommentCreateDto commentCreateDto, Book book) {
        return Comment.builder()
                .text(commentCreateDto.getText())
                .book(book)
                .build();
    }

    public CommentUpdateDto toUpdateDto(Comment comment) {
        return CommentUpdateDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build();
    }
}
