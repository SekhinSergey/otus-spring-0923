package ru.otus.spring.service;

import ru.otus.spring.dto.response.GenreDto;

import java.util.List;

public interface GenreService {

    List<GenreDto> findAll();
}
