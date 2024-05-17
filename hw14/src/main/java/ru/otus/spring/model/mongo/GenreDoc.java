package ru.otus.spring.model.mongo;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "genres")
public class GenreDoc {

    @Id
    private String id;

    private String name;
}
