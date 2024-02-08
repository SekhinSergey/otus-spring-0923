package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Genre;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookDtoServiceImpl implements BookDtoService {

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public Author getAuthorByBookDtoAuthorId(Long id) {
        return authorService.findById(id);
    }

    @Override
    public Set<Genre> getGenresByBookDtoGenreIds(Set<Long> ids) {
        return ids.stream()
                .map(genreService::findById)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
