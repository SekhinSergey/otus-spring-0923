package ru.otus.spring.model.mongo;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Document(collection = "authors")
public class AuthorDoc {

    @Id
    private String id;

    @Field("full_name")
    private String fullName;
}
