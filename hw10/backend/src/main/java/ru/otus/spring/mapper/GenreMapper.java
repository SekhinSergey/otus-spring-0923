package ru.otus.spring.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.create.GenreCreateDto;
import ru.otus.spring.dto.update.GenreUpdateDto;
import ru.otus.spring.model.Genre;

@Component
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public GenreCreateDto toCreateDto(Genre genre) {
        return GenreCreateDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public Genre createDtoToEntity(GenreCreateDto genreCreateDto) {
        return Genre.builder()
                .id(genreCreateDto.getId())
                .name(genreCreateDto.getName())
                .build();
    }

    public GenreUpdateDto toUpdateDto(Genre genre) {
        return GenreUpdateDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public Genre updateDtoToEntity(GenreUpdateDto genreUpdateDto) {
        return Genre.builder()
                .id(genreUpdateDto.getId())
                .name(genreUpdateDto.getName())
                .build();
    }
}
