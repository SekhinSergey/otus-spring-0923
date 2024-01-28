package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final String OR_ELSE_THROW_RULE = "java:S2201";

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @SuppressWarnings(OR_ELSE_THROW_RULE)
    public void validateBook(Book book) {
        Long authorId = book.getAuthor().getId();
        authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        Set<Long> genresIds = book.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toCollection(HashSet::new));
        var foundGenres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != foundGenres.size()) {
            throw new EntityNotFoundException(
                    "The number of requested genres does not match the number in the database");
        }
    }
}
