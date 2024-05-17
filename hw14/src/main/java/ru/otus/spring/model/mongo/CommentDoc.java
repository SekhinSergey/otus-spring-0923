package ru.otus.spring.model.mongo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "comments")
public class CommentDoc {

    @Id
    private String id;

    private String text;

    @DBRef
    private BookDoc book;

    @DBRef
    private UserDoc user;
}
