package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;

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

    public BookCreateDto toCreateDto(Book book) {
        String genreIds = book.getGenres()
                .stream()
                .map(genre -> genre.getId().toString() + SPACE)
                .collect(Collectors.joining())
                .trim();
        return BookCreateDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
    }

    public Book createDtoToEntity(BookCreateDto bookCreateDto, Author author, Set<Genre> genres) {
        return Book.builder()
                .id(bookCreateDto.getId())
                .title(bookCreateDto.getTitle())
                .author(author)
                .genres(genres)
                .build();
    }

    public BookUpdateDto toUpdateDto(Book book) {
        String genreIds = book.getGenres()
                .stream()
                .map(genre -> genre.getId().toString() + SPACE)
                .collect(Collectors.joining())
                .trim();
        return BookUpdateDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
    }

    public Book updateDtoToEntity(BookUpdateDto bookUpdateDto, Author author, Set<Genre> genres) {
        return Book.builder()
                .id(bookUpdateDto.getId())
                .title(bookUpdateDto.getTitle())
                .author(author)
                .genres(genres)
                .build();
    }
}
