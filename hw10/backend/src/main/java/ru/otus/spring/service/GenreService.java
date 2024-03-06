package ru.otus.spring.service;

import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.create.GenreCreateDto;
import ru.otus.spring.dto.update.GenreUpdateDto;

import java.util.List;
import java.util.Set;

public interface GenreService {

    List<GenreDto> findAll();

    GenreDto findById(long id);

    List<GenreDto> findAllByIds(Set<Long> ids);

    GenreCreateDto create(GenreCreateDto genreCreateDto);

    GenreUpdateDto update(GenreUpdateDto genreUpdateDto);

    List<GenreCreateDto> createBatch(Set<GenreCreateDto> genres);

    List<GenreUpdateDto> updateBatch(Set<GenreUpdateDto> genreUpdateDtos);
}
