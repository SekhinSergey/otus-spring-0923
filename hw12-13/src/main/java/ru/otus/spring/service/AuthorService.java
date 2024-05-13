package ru.otus.spring.service;

import ru.otus.spring.dto.response.AuthorDto;

import java.util.List;

public interface AuthorService {

    List<AuthorDto> findAll();
}
