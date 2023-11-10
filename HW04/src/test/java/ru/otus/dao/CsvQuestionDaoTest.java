package ru.otus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.config.TestFileNameProvider;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;

@DisplayName("QuestDaoCsv")
@SpringBootTest(classes = {CsvQuestionDao.class, TestFileNameProvider.class } )
class CsvQuestionDaoTest {


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

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private TestFileNameProvider testFileNameProvider;
    private final String fileName = "test-questions.csv";

    @BeforeEach
    void setUp() {
        BDDMockito.given(testFileNameProvider.getTestFileName()).willReturn(fileName);

    }

    @DisplayName("should be create correct List<Question> for given text")
    @Test
    void getAllTestWithMockConverter() {
        List<Question> actualQuests = questionDao.findAll();

        Assertions.assertIterableEquals(actualQuests, questionList);
    }


}