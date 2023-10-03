package ru.otus.service;

import ru.otus.dao.QuestDao;
import ru.otus.model.Question;

import java.util.stream.Collectors;

public class QuestionServiceImpl implements QuestionService {

    private static final String TEMPLATE_FULL_TEXT_OF_QUEST = "%d: %s\n%s";

    private static final String TEMPLATE_TEXT_OF_ANSWERS = "%s\n";

    private final QuestDao questDao;

    private final IOService ioService;

    public QuestionServiceImpl(QuestDao questDao, IOService ioService) {
        this.questDao = questDao;
        this.ioService = ioService;
    }

    @Override
    public void outAllQuestions() {
        questDao.getAll()
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
