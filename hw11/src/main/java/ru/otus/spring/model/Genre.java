package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@Document(collection = "genres")
public class Genre {

    @Id
    private String id;

    private String name;
}
