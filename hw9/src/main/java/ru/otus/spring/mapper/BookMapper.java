package ru.otus.spring.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.GenreService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorService authorService;

    private final GenreService genreService;

    public BookDto mapEntityToDto(Book book) {
        String genreIds = book.getGenres()
                .stream()
                .map(genre -> genre.getId().toString() + SPACE)
                .collect(Collectors.joining())
                .trim();
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
    }

    public Book mapDtoToEntity(BookDto bookDto) {
        Author author = authorService.findById(bookDto.authorId());
        List<Genre> genres = Arrays.stream(bookDto.genreIds()
                .split(SPACE))
                .toList().stream()
                    .map(id -> genreService.findById(Long.parseLong(id)))
                    .toList();
        return Book.builder()
                .id(bookDto.id())
                .title(bookDto.title())
                .author(author)
                .genres(genres)
                .build();
    }
}
