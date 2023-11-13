package ru.otus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.config.TestFileNameProvider;
import ru.otus.fixtures.Quests;
import ru.otus.model.Question;

import java.util.List;

@DisplayName("QuestDaoCsv")
@SpringBootTest(classes = {CsvQuestionDao.class, TestFileNameProvider.class } )
class CsvQuestionDaoTest {




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
        List<Question> questionList = Quests.getList();

        List<Question> actualQuests = questionDao.findAll();

        Assertions.assertIterableEquals(actualQuests, questionList);
    }


}