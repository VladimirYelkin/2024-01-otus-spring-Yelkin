package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Question;
import ru.otus.service.io.IOService;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String TEMPLATE_FULL_TEXT_OF_QUEST = "%d: %s\n%s";

    private static final String TEMPLATE_TEXT_OF_ANSWERS_WITH_ID = "%d:%s\n";

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

    @Override
    public void outQuestion(Question question) {
        AtomicInteger idOfAnswer = new AtomicInteger(1);
        String textOfAnswers = question.answers().stream()
                .map(answer ->
                        TEMPLATE_TEXT_OF_ANSWERS_WITH_ID.formatted(idOfAnswer.getAndIncrement(), answer.textOfAnswer()))
                .collect(Collectors.joining());
        ioService.printLine(TEMPLATE_FULL_TEXT_OF_QUEST.formatted(
                question.id(), question.textOfQuestion(), textOfAnswers));
    }


    @Override
    public boolean checkAnswer(Question question, int idAnswer) {
        return question.answers().get(idAnswer - 1).correct();
    }

    @Override
    public int getMaxIndexAnswers(Question question) {
        return question.answers().size();
    }
}
