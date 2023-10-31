package ru.otus.spring.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

import ru.otus.dao.QuestionDao;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.model.Student;
import ru.otus.model.TestResult;
import ru.otus.service.TestService;
import ru.otus.service.TestServiceImpl;
import ru.otus.service.localize.LocalizedIOService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестируемый класс TestServiceImpl ")
class TestServiceImplTest {
    TestService testService;
    @Mock
    private QuestionDao questionDao;

    @Mock
    private LocalizedIOService ioService;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(questionDao, ioService);
        inOrder = inOrder(ioService);

    }

    @Test
    @DisplayName(" должен корректно проводить тест и возвращать результат")
    void shouldExecuteTestForStudent() {
        Student student = new Student("FirstNameOfStudent", "SecondName");
        TestResult expectedResult = new TestResult(student);
        List <Question> questionList = generateQuestions();
        ReflectionTestUtils.setField(expectedResult, "rightAnswersCount", 2);
        ReflectionTestUtils.setField(expectedResult, "answeredQuestions", questionList);
        when(questionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPromptLocalized
                (anyInt(), anyInt(), eq ("TestService.input.answer.id"), eq ("TestService.error.answer.id")))
                .thenReturn(2);

        TestResult actual = testService.executeTestFor(student);

        inOrder.verify(ioService, times(1)).printLine("");
        inOrder.verify(ioService, times(1)).printLineLocalized(eq ("TestService.answer.the.questions"));
        inOrder.verify(ioService, times(1)).printLine("");
        inOrder.verify(ioService, times(5)).printLine(anyString());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @AfterEach
    void tearDown() {
    }

    private List<Question> generateQuestions() {
        return List.of(
                new Question(1L, "question1",
                        List.of(
                                new Answer("answer11", true),
                                new Answer("answer12"),
                                new Answer("answer13")
                        )
                ),
                new Question(2L, "question2",
                        List.of(
                                new Answer("answer21"),
                                new Answer("answer22", true),
                                new Answer("answer23")
                        )
                ),
                new Question(3L, "question3",
                        List.of(
                                new Answer("answer31"),
                                new Answer("answer32", true),
                                new Answer("answer33")
                        )
                ),
                new Question(4L, "question4",
                        List.of(
                                new Answer("answer41"),
                                new Answer("answer42"),
                                new Answer("answer43", true)
                        )
                ),
                new Question(5L, "question5",
                        List.of(
                                new Answer("answer51"),
                                new Answer("answer52"),
                                new Answer("answer53", true)
                        )
                )
        );
    }
}