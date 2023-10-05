package ru.otus.service;

import ru.otus.dao.QuestionDao;
import ru.otus.model.Question;

import java.util.stream.Collectors;

public class QuestionServiceImpl implements QuestionService {

    private static final String TEMPLATE_FULL_TEXT_OF_QUEST = "%d: %s\n%s";

    private static final String TEMPLATE_TEXT_OF_ANSWERS = "%s\n";

    private final QuestionDao questionDao;

    private final IOService ioService;

    public QuestionServiceImpl(QuestionDao questionDao, IOService ioService) {
        this.questionDao = questionDao;
        this.ioService = ioService;
    }

    @Override
    public void printQuestions() {
        questionDao.findAll()
                .forEach(this::outQuestion);
    }

    private void outQuestion(Question question) {
        String textOfAnswers = question.answers().stream()
                .map(answer -> TEMPLATE_TEXT_OF_ANSWERS.formatted(answer.textOfAnswer()))
                .collect(Collectors.joining());
        ioService.println(TEMPLATE_FULL_TEXT_OF_QUEST.formatted(
                question.id(), question.textOfQuestion(), textOfAnswers));
    }
}
