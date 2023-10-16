package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-spring-context.xml")
class QuestionDaoTest {

    @Autowired
    @Qualifier("questionDaoEmptyResult")
    private QuestionDao questionDaoEmptyResult;

    @Autowired
    @Qualifier("questionDaoErrorResult")
    private QuestionDao questionDaoErrorResult;

    @Autowired
    @Qualifier("questionDaoGoodResult")
    private QuestionDao questionDaoGoodResult;

    @Test
    void assertNotNullTestBeans() {
        assertNotNull(questionDaoEmptyResult);
        assertNotNull(questionDaoErrorResult);
        assertNotNull(questionDaoGoodResult);
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsv() {
        assertDoesNotThrowAndReturnResult(questionDaoEmptyResult, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByNoDelimiterCsv() {
        assertEquals("Column with index 1 not found in CSV",
                assertThrows(CsvReadException.class, () -> questionDaoErrorResult.getAll()).getMessage());
    }

    @Test
    void assertReturnListByGoodCsv() {
        Question question = Question.builder()
                .text("1")
                .answers(Arrays.asList(new Answer("2"), new Answer("3"), new Answer(false)))
                .build();
        assertDoesNotThrowAndReturnResult(questionDaoGoodResult, Collections.singletonList(question));
    }

    private void assertDoesNotThrowAndReturnResult(QuestionDao questionDao, List<Question> expected) {
        assertDoesNotThrow(() -> {
            questionDao.getAll();
        });
        assertEquals(expected, questionDao.getAll());
    }
}
