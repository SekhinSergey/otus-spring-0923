package ru.otus.spring.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.List;

@Builder
@EqualsAndHashCode
public class Question {
    private String text;

    private List<Answer> answers;

    @Override
    public String toString() {
        return "Question: " + text + "\n"
                + "Student answer: " + answers.get(0) + "\n"
                + "Right answer: " + answers.get(1) + "\n"
                + "Result: " + answers.get(2) + "\n";
    }
}
