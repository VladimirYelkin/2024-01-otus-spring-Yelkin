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

    private final QuestionDao questionDao;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        questions.forEach(question -> {
            questionService.outQuestion(question);
            int maxIdOfAnswer = questionService.getMaxIndexAnswers(question);
            int idAnswer = ioService.readIntForRangeWithPrompt(1, maxIdOfAnswer,
                    "Input number of answer (1-%d): ".formatted(maxIdOfAnswer), "not correct answer");
            boolean isAnswerValid = questionService.checkAnswer(question, idAnswer);
            testResult.applyAnswer(question, isAnswerValid);
        });
        return testResult;
    }
}
