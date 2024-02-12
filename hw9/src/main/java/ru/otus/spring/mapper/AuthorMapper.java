package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.create.AuthorCreateDto;
import ru.otus.spring.dto.update.AuthorUpdateDto;
import ru.otus.spring.model.Author;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }

    public AuthorCreateDto toCreateDto(Author author) {
        return AuthorCreateDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }

    public Author createDtoToEntity(AuthorCreateDto authorCreateDto) {
        return Author.builder()
                .id(authorCreateDto.getId())
                .fullName(authorCreateDto.getFullName())
                .build();
    }

    public AuthorUpdateDto toUpdateDto(Author author) {
        return AuthorUpdateDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }

    public Author updateDtoToEntity(AuthorUpdateDto authorUpdateDto) {
        return Author.builder()
                .id(authorUpdateDto.getId())
                .fullName(authorUpdateDto.getFullName())
                .build();
    }
}
