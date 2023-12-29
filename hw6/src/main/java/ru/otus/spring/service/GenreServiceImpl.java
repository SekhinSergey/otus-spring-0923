package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIds(ids);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public Genre save(Genre genre) {
        Genre savedGenre = genreRepository.save(genre);
        if (isNull(savedGenre)) {
            throw new EntityNotFoundException("An error occurred when trying to save the genre with name %s"
                    .formatted(genre.getName()));
        }
        return savedGenre;
    }
}
