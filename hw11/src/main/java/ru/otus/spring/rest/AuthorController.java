package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.dto.response.AuthorDto;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.Comparator;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository repository;

    @GetMapping("/api/library/authors")
    public Flux<AuthorDto> getAll() {
        return repository.findAll()
                .map(this::toDto)
                .sort(Comparator.comparing(AuthorDto::id));
    }

    private AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
