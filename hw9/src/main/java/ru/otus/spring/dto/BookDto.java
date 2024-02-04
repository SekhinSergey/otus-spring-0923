package ru.otus.spring.dto;

import lombok.Builder;

@Builder
public record BookDto(long id, String title, long authorId, String genreIds){}
