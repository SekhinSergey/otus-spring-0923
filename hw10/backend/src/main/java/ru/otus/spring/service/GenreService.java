package ru.otus.spring.service;

import ru.otus.spring.dto.response.GenreDto;
import ru.otus.spring.dto.create.GenreCreateDto;
import ru.otus.spring.dto.update.GenreUpdateDto;

import java.util.List;
import java.util.Set;

public interface GenreService {

    List<GenreDto> findAll();

    GenreDto findById(long id);

    List<GenreDto> findAllByIds(Set<Long> ids);

    GenreDto create(GenreCreateDto genreCreateDto);

    GenreDto update(GenreUpdateDto genreUpdateDto);

    List<GenreDto> createBatch(Set<GenreCreateDto> genres);

    List<GenreDto> updateBatch(Set<GenreUpdateDto> genreUpdateDtos);
}
