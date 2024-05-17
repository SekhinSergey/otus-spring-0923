package ru.otus.spring.model.mongo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@Document(collection = "books")
public class BookDoc {

    @Id
    private String id;

    private String title;

    private AuthorDoc author;

    private List<GenreDoc> genres;
}
