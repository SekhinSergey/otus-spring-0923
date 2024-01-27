package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@AllArgsConstructor
@Document(collection = "authors")
public class Author {

    @Id
    private String id;

    @Field("full_name")
    private String fullName;
}
