package ru.otus.spring.mapper;

import org.mapstruct.Mapper;
import ru.otus.spring.model.jpa.Author;
import ru.otus.spring.model.mongo.AuthorDoc;

@Mapper
public interface AuthorMapper {
    AuthorDoc toDoc(Author author);
}
