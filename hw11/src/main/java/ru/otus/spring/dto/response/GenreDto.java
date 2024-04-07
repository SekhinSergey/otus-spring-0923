package ru.otus.spring.dto.response;

import lombok.Builder;

@Builder
public record GenreDto(String id, String name) {
}
