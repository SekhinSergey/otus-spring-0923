package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.AuthorDto;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
