package ru.otus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import ru.otus.converter.Converter;
import ru.otus.converter.ConverterCSV;
import ru.otus.dataservice.ReaderResource;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;

@DisplayName("QuestDaoCsv")
class QuestDaoCsvTest {

    private final List<String> dataFromResources = List.of(
            "1;question1;answer11:1;answer12;answer13",
            "2;question2;answer21;answer22:1;answer23",
            "3;question3;answer31;answer32:1;answer33",
            "4;question4;answer41;answer42;answer43:1",
            "5;question5;answer51;answer52;answer53:1");

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

    private ReaderResource readerResource;
    private Converter converter;

    private QuestDao questDao;

    @BeforeEach
    void setUp() {
        readerResource = Mockito.mock(ReaderResource.class);
    }

    @DisplayName("should be create correct List<Question> for given text")
    @Test
    void getAllTestWithMockConverter() {
        converter = Mockito.mock(Converter.class);
        questDao = new QuestDaoCsv(readerResource, converter);
        BDDMockito.given(readerResource.getLines()).willReturn(dataFromResources);
        for (int i = 0; i < dataFromResources.size(); i++) {
            BDDMockito.given(converter.covertToQuest(dataFromResources.get(i))).willReturn(questionList.get(i));
        }
        List<Question> actualQuests = questDao.getAll();
        Assertions.assertIterableEquals(actualQuests, questionList);
    }

    @DisplayName("should be create correct List<Question> for given text with Spy Converter")
    @Test
    void getAllTestWithSpyConverter() {
        converter = Mockito.spy(ConverterCSV.class);
        questDao = new QuestDaoCsv(readerResource, converter);
        BDDMockito.given(readerResource.getLines()).willReturn(dataFromResources);
        List<Question> actualQuests = questDao.getAll();
        Assertions.assertIterableEquals(actualQuests, questionList);
        Mockito.verify(converter, Mockito.times(dataFromResources.size())).covertToQuest(Mockito.anyString());
    }

}