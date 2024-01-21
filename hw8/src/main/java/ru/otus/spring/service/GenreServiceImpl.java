package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return genreRepository.findAllByIdIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> findByExample(Genre genre) {
        return genreRepository.findOne(Example.of(genre));
    }

    @Override
    @Transactional
    public List<Genre> saveBatch(Set<Genre> genres) {
        return genreRepository.saveAll(genres);
    }
}
