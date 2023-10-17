package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Question;
import ru.otus.service.io.IOService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private static final String TEMPLATE_FULL_TEXT_OF_QUEST = "%d: %s\n%s";

    private static final String TEMPLATE_TEXT_OF_ANSWERS_WITH_ID = "%d:%s\n";

    private final QuestionDao questionDao;

    private final IOService ioService;

    @Override
    public void printQuestions() {
        questionDao.findAll().forEach(this::outQuestion);
    }

    @Override
    public List<Question> findAll() {
        return questionDao.findAll();
    }

    @Override
    public void outQuestion(Question question) {
        var idOfAnswer = new AtomicInteger(1);
        var textOfAnswers = question.answers().stream()
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
