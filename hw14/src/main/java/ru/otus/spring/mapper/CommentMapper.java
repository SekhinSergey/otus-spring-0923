package ru.otus.spring.mapper;

import org.mapstruct.Mapper;
import ru.otus.spring.model.jpa.Comment;
import ru.otus.spring.model.mongo.CommentDoc;

@Mapper
public interface CommentMapper {
    CommentDoc toDoc(Comment comment);
}
