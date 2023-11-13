package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public List<Genre> findAllByIds(List<Long> ids) {
        return genreRepository.findAllByIds(ids);
    }

    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    public Genre insert(Genre genre) {
        return genreRepository.insert(genre);
    }
}
