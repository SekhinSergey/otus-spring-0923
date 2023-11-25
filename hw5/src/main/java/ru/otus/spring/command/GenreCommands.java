package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        List<Genre> genres = genreService.findAll();
        if (genres.isEmpty()) {
            return "No genres found";
        }
        return genres.stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all genres by ids", key = "agbyids")
    public String findAllGenresByIds(Set<Long> ids) {
        List<Genre> genres = genreService.findAllByIds(ids);
        if (genres.isEmpty()) {
            return "No genres with ids %s found".formatted(ids);
        }
        return genres.stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find genre by name", key = "agbyn")
    public String findByName(String name) {
        return genreService.findByName(name)
                .map(genreConverter::genreToString)
                .orElse("Genre with name %s not found".formatted(name));
    }

    @ShellMethod(value = "Insert and get genre", key = "gins")
    public Genre insert(Genre genre) {
        return genreService.insert(genre);
    }
}
