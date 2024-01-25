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
    @ShellMethod(value = "Create and get genre", key = "gnew")
    public String createGenre(Genre genre) {
        return genreConverter.genreToString(genreService.create(genre));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update and get genre", key = "gupd")
    public String updateGenre(Genre genre) {
        return genreConverter.genreToString(genreService.update(genre));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create genre batch", key = "gnewb")
    public String createGenreBatch(Set<Genre> genres) {
        return genreService.createBatch(genres).stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update genre batch", key = "gupdb")
    public String updateGenreBatch(Set<Genre> genres) {
        return genreService.updateBatch(genres).stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
