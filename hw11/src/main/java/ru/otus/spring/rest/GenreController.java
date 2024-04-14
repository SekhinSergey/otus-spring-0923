package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.dto.response.GenreDto;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.Comparator;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository repository;

    @GetMapping("/api/library/genres")
    public Flux<GenreDto> getAll() {
        return repository.findAll()
                .map(this::toDto)
                .sort(Comparator.comparing(GenreDto::id));
    }

    private GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
