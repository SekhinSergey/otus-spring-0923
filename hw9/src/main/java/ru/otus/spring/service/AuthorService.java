package ru.otus.spring.service;

import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.create.AuthorCreateDto;
import ru.otus.spring.dto.update.AuthorUpdateDto;

import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public interface AuthorService {

    List<AuthorDto> findAll();

    AuthorDto findById(Long id);

    List<AuthorDto> findAllByIds(Set<Long> ids);

    AuthorCreateDto create(AuthorCreateDto authorCreateDto);

    AuthorUpdateDto update(AuthorUpdateDto authorUpdateDto);

    List<AuthorCreateDto> createBatch(Set<AuthorCreateDto> authorCreateDtos);

    List<AuthorUpdateDto> updateBatch(Set<AuthorUpdateDto> authorUpdateDtos);
}
