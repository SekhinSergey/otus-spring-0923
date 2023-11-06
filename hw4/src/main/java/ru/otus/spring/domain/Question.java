package ru.otus.spring.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Question {

    private String text;

    private String rightAnswer;
}
