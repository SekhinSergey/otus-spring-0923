package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all genres by ids", key = "agbyids")
    public String findAllGenresByIds(List<Long> ids) {
        return genreService.findAllByIds(ids).stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find genre by name", key = "agbyn")
    public Optional<Genre> findByName(String name) {
        return genreService.findByName(name);
    }

    @ShellMethod(value = "Insert and get genre", key = "gins")
    public Genre insert(Genre genre) {
        return genreService.insert(genre);
    }
}
