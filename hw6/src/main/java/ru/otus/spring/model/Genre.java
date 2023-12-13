package ru.otus.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public static Map<Long, Genre> buildGenreMap(List<Genre> genres) {
        return genres.stream().collect(Collectors.toMap(Genre::getId, genre -> genre, (a, b) -> b));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Genre genre = (Genre) o;
        if (o == null || getClass() != o.getClass() || id != genre.id) {
            return false;
        }
        return name.equals(genre.name);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
