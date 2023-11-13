package ru.otus.spring.converter;

import org.springframework.stereotype.Component;
import ru.otus.spring.model.Genre;

@Component
public class GenreConverter {

    public String genreToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
