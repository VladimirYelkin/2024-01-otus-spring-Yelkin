package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.io.IOService;

import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

class QuestionServiceImplTest {

    private QuestionDao questionDao;

    private IOService ioService;
    private QuestionService questionService;

    private InOrder inOrder;

    private final List<Question> questionList = List.of(
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


    @BeforeEach
    void setUp() {
        questionDao = Mockito.mock(QuestionDao.class);
        ioService = Mockito.mock(IOService.class);
        questionService = new QuestionServiceImpl(questionDao, ioService);
        inOrder = inOrder(questionDao,ioService);
    }

    @DisplayName("Should be correct out data ")
    @Test
    void outAllQuestions() {
        BDDMockito.given(questionDao.findAll()).willReturn(questionList);
        questionService.printQuestions();
        inOrder.verify(questionDao,times(1)).findAll();
        inOrder.verify(ioService,times(questionList.size())).printLine(Mockito.anyString());
    }
}