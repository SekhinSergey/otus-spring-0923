package ru.otus.spring.dto.response;

import lombok.Builder;

@Builder
public record CommentDto(Long id, String text, Long bookId) {
}
