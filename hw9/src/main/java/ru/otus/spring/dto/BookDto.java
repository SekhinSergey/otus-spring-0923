package ru.otus.spring.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record BookDto(Long id, String title, Long authorId, Set<Long> genreIds) {
}
