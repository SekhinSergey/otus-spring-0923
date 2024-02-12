package ru.otus.spring.dto;

import lombok.Builder;

@Builder
public record GenreDto(Long id, String name) {
}
