package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import ru.otus.dao.QuestionDao;
import ru.otus.fixtures.Quests;
import ru.otus.model.Question;
import ru.otus.model.Student;
import ru.otus.model.TestResult;
import ru.otus.service.localize.LocalizedIOService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Тестируемый класс TestServiceImpl ")
@SpringBootTest
class TestServiceImplTest {

    @Autowired
    TestService testService;

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private LocalizedIOService ioService;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = inOrder(ioService);

    }

    @Test
    @DisplayName(" должен корректно проводить тест и возвращать результат")
    void shouldExecuteTestForStudent() {
        Student student = new Student("FirstNameOfStudent", "SecondName");
        TestResult expectedResult = new TestResult(student);
        List <Question> questionList = Quests.getList();
        ReflectionTestUtils.setField(expectedResult, "rightAnswersCount", 2);
        ReflectionTestUtils.setField(expectedResult, "answeredQuestions", questionList);
        when(questionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPromptLocalized
                (anyInt(), anyInt(), eq ("TestService.input.answer.id"), eq ("TestService.error.answer.id")))
                .thenReturn(2);

        TestResult actual = testService.executeTestFor(student);

        inOrder.verify(ioService, times(1)).printLine("");
        inOrder.verify(ioService, times(1)).printLineLocalized( ("TestService.answer.the.questions"));
        inOrder.verify(ioService, times(1)).printLine("");
        inOrder.verify(ioService, times(5)).printLine(anyString());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedResult);
    }

}