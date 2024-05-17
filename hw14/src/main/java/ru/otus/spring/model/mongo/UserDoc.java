package ru.otus.spring.model.mongo;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "users")
public class UserDoc {

    @Id
    private String id;

    private String email;

    private String roles;

    private String password;
}
