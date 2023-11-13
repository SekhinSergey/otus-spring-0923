package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Genre {

    private long id;

    private String name;

    public static Map<Long, Genre> buildGenreMap(List<Genre> genres) {
        return genres.stream().collect(Collectors.toMap(Genre::getId, genre -> genre, (a, b) -> b));
    }
}
