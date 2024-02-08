package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        Set<Long> genreIds = book.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
    }

    public Book toEntity(BookDto bookDto, Author author, Set<Genre> genres) {
        return Book.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .author(author)
                .genres(genres)
                .build();
    }
}
