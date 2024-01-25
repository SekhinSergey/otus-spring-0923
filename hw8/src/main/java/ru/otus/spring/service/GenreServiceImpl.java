package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> findAllByIds(Set<String> ids) {
        return genreRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public Genre update(Genre genre) {
        genreRepository.findById(genre.getId());
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public List<Genre> createBatch(Set<Genre> genres) {
        return genreRepository.saveAll(genres);
    }

    @Override
    @Transactional
    public List<Genre> updateBatch(Set<Genre> genres) {
        validateGenres(genres);
        return genreRepository.saveAll(genres);
    }

    private void validateGenres(Set<Genre> genres) {
        Set<String> genresIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toCollection(HashSet::new));
        List<Genre> foundGenres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != foundGenres.size()) {
            throw new EntityNotFoundException(
                    "The number of requested genres does not match the number in the database");
        }
    }
}
