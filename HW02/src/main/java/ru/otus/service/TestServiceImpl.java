package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Student;
import ru.otus.model.TestResult;
import ru.otus.service.io.IOService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {


    private final QuestionService questionService;

    private final IOService ioService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionService.findAll();
        var testResult = new TestResult(student);

        questions.forEach(question -> {
            questionService.outQuestion(question);
            int maxIndexAnswers = questionService.getMaxIndexAnswers(question);
            int idAnswer = readAnswerFromStudent(1,maxIndexAnswers);
            boolean isAnswerValid = questionService.checkAnswer(question, idAnswer);
            testResult.applyAnswer(question, isAnswerValid);
        });
        return testResult;
    }

    private int readAnswerFromStudent (int startIndexOfAnswers, int endIndexOfAnswers) {
        return ioService.readIntForRangeWithPrompt(startIndexOfAnswers, endIndexOfAnswers,
                "Input number of answer (%d-%d): ".formatted(startIndexOfAnswers,endIndexOfAnswers), "not correct answer");
    }
}
