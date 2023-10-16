package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.out.Printer;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    private final Printer printer;

    public QuestionServiceImpl(QuestionDao questionDao, Printer printer) {
        this.questionDao = questionDao;
        this.printer = printer;
    }

    public void printQuestion() throws CsvReadException {
        for (Question question : questionDao.getAll()) {
            printer.print(question);
        }
    }
}
