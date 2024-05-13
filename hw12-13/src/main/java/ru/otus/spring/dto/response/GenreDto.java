package ru.otus.spring.dto.response;

import lombok.Builder;

@Builder
public record GenreDto(Long id, String name) {
}
