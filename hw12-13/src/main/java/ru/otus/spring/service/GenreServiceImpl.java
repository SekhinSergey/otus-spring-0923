package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.GenreDto;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
