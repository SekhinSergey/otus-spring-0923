package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJdbc implements GenreRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select id, name from genres", new GenreRowMapper());
    }

    public List<Genre> findAllByIds(Set<Long> ids) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcOperations.query(
                "select id, name from genres where id in (:ids)",
                parameters,
                new GenreRowMapper());
    }

    public Optional<Genre> findByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        Genre genre;
        try {
            genre = namedParameterJdbcOperations.queryForObject(
                    "select id, name from genres where name = :name", params, new GenreRowMapper());
        } catch (Exception exception) {
            return Optional.empty();
        }
        return Optional.ofNullable(genre);
    }

    public Genre insert(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into genres name values :name",
                params,
                keyHolder,
                new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        if (isNull(id)) {
            throw new EntityNotFoundException("Genre id is null in new genre");
        }
        genre.setId(id);
        return genre;
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        public Genre mapRow(ResultSet rs, int rowNumber) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
