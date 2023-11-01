package ru.otus.spring.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestResult {

    private User user;

    private int rightAnswerCount;

    private List<Question> unansweredQuestions;

    public TestResult(User user) {
        this.user = user;
        unansweredQuestions = new ArrayList<>();
    }

    public static int checkTheAnswer(int rightAnswerCount,
                                      TestResult testResult,
                                      Question question,
                                      String studentAnswer) {
        String rightAnswer = question.getRightAnswer();
        if (rightAnswer.equals(studentAnswer)) {
            rightAnswerCount++;
        } else {
            testResult.getUnansweredQuestions().add(question);
        }
        return rightAnswerCount;
    }
}
