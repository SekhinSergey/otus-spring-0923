package ru.otus.spring.mapper;

import org.mapstruct.Mapper;
import ru.otus.spring.model.jpa.Genre;
import ru.otus.spring.model.mongo.GenreDoc;

@Mapper
public interface GenreMapper {
    GenreDoc toDoc(Genre genre);
}
