package ru.otus.spring.domain;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Survey {
    private String question;

    private String badAnswer;

    private String goodAnswer;
}