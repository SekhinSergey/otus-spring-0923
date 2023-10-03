package ru.otus.spring.domain;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Survey {
    private Question question;

    private Answer badAnswer;

    private Answer goodAnswer;
}
