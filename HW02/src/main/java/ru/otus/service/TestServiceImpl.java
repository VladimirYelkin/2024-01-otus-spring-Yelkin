package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Question;
import ru.otus.model.Student;
import ru.otus.model.TestResult;
import ru.otus.service.io.IOService;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TEMPLATE_FULL_TEXT_OF_QUEST = "%d: %s\n%s";

    private static final String TEMPLATE_TEXT_OF_ANSWERS_WITH_ID = "%d:%s\n";

    private final QuestionDao questionDao;

    private final IOService ioService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        questions.forEach(question -> {
            outQuestion(question);
            var idAnswer = readIdOfAnswerFromInput(1, getMaxIndexAnswers(question));
            var isAnswerValid = checkAnswer(question, idAnswer);
            testResult.applyAnswer(question, isAnswerValid);
        });
        return testResult;
    }

    private int readIdOfAnswerFromInput(int startIndexOfAnswers, int endIndexOfAnswers) {
        return ioService.readIntForRangeWithPrompt(startIndexOfAnswers, endIndexOfAnswers,
                "Input number of answer (%d-%d): ".formatted(startIndexOfAnswers, endIndexOfAnswers)
                , "not correct answer");
    }

    private void outQuestion(Question question) {
        var idOfAnswer = new AtomicInteger(1);
        var textOfAnswers = question.answers().stream()
                .map(answer ->
                        TEMPLATE_TEXT_OF_ANSWERS_WITH_ID.formatted(idOfAnswer.getAndIncrement(), answer.textOfAnswer()))
                .collect(Collectors.joining());
        ioService.printLine(TEMPLATE_FULL_TEXT_OF_QUEST.formatted(
                question.id(), question.textOfQuestion(), textOfAnswers));
    }

    private boolean checkAnswer(Question question, int idAnswer) {
        return question.answers().get(idAnswer - 1).correct();
    }

    private int getMaxIndexAnswers(Question question) {
        return question.answers().size();
    }
}
