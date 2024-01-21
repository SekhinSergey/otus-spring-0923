package ru.otus.spring.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.model.Author;

@Component
public class AuthorConverter {

    public String authorToString(Author author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
