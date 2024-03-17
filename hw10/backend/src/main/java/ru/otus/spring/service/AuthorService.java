package ru.otus.spring.service;

import ru.otus.spring.dto.response.AuthorDto;
import ru.otus.spring.dto.create.AuthorCreateDto;
import ru.otus.spring.dto.update.AuthorUpdateDto;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    List<AuthorDto> findAll();

    AuthorDto findById(long id);

    List<AuthorDto> findAllByIds(Set<Long> ids);

    AuthorDto create(AuthorCreateDto authorCreateDto);

    AuthorDto update(AuthorUpdateDto authorUpdateDto);

    List<AuthorDto> createBatch(Set<AuthorCreateDto> authorCreateDtos);

    List<AuthorDto> updateBatch(Set<AuthorUpdateDto> authorUpdateDtos);
}
