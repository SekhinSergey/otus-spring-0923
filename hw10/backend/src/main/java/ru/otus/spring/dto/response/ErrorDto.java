package ru.otus.spring.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record ErrorDto(Set<Error> errors) {
    @Builder
    public record Error(String field, String message) {
    }
}
