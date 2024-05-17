package ru.otus.spring.mapper;


import org.mapstruct.Mapper;
import ru.otus.spring.model.jpa.Book;
import ru.otus.spring.model.mongo.BookDoc;

@Mapper
public interface BookMapper {
    BookDoc toDoc(Book book);
}
