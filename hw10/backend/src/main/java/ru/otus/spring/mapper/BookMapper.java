package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        Set<Long> genreIds = book.getGenres().stream()
                .map(Genre::getId)
                .collect(toSet());
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
    }

    public Book createDtoToEntity(BookCreateDto bookCreateDto, Author author, Set<Genre> genres) {
        return Book.builder()
                .title(bookCreateDto.getTitle())
                .author(author)
                .genres(genres)
                .build();
    }
}
