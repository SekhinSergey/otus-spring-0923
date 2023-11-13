package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJdbc implements AuthorRepository {

    private static final String FULL_NAME = "full_name";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("select id, full_name from authors", new AuthorRowMapper());
    }

    public Optional<Author> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                "select id, full_name from authors where id = :id", params, new AuthorRowMapper()
        ));
    }

    public Optional<Author> findByFullName(String fullName) {
        Map<String, Object> params = Collections.singletonMap(FULL_NAME, fullName);
        return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                "select id, full_name from authors where full_name = :full_name", params, new AuthorRowMapper()
        ));
    }

    public Author insert(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(FULL_NAME, author.getFullName());
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into authors full_name values :full_name",
                params,
                keyHolder,
                new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        if (isNull(id)) {
            throw new EntityNotFoundException("Author id is null in new author");
        }
        author.setId(id);
        return author;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        public Author mapRow(ResultSet rs, int rowNumber) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString(FULL_NAME);
            return new Author(id, fullName);
        }
    }
}
