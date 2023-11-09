package ru.otus.spring.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.HomeWork04;
import ru.otus.dao.QuestionDao;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;

@DisplayName("Метод сервиса чтения вопросов должен ")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HomeWork04.class, TestContextConfig.class})
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

    @DisplayName(" вернуть корректный список вопросов")
    @Test
    void getAllTestWithMockConverter() {
        List<Question> actualQuests = questionDao.findAll();
        Assertions.assertIterableEquals(actualQuests, questionList);
    }


}