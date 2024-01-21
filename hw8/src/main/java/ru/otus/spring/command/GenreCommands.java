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

    @SuppressWarnings("all")
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

    @SuppressWarnings("all")
    @ShellMethod(value = "Find all genres by ids", key = "agbyids")
    public String findAllGenresByIds(Set<String> ids) {
        List<Genre> genres = genreService.findAllByIds(ids);
        if (genres.isEmpty()) {
            return "No genres with ids %s found".formatted(ids);
        }
        return genres.stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find genre by name", key = "agbyn")
    public String findGenreByName(String name) {
        return genreService.findByName(name)
                .map(genreConverter::genreToString)
                .orElse("Genre with name %s not found".formatted(name));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Save and get genre", key = "gins")
    public String saveGenre(Genre genre) {
        return genreConverter.genreToString(genreService.save(genre));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find genre by example", key = "gbye")
    public String findByGenreExample(Genre genre) {
        return genreService.findByExample(genre)
                .map(genreConverter::genreToString)
                .orElse("Genre with name %s not found".formatted(genre.getName()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Save genre batch", key = "ginsb")
    public String saveGenreBatch(Set<Genre> genres) {
        List<Genre> savedGenres = genreService.saveBatch(genres);
        if (savedGenres.isEmpty()) {
            return "No genres saved";
        }
        return savedGenres.stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
