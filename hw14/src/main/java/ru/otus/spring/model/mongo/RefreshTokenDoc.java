package ru.otus.spring.model.mongo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "tokens")
public class RefreshTokenDoc {

    @Id
    private String id;

    private UserDoc user;

    private String token;

    private String revoked;
}
