package ru.otus.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.exeption.QuestionDaoException;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;


@DisplayName("ConverterCSV")
class ConverterCSVTest {
    public static final String STRING_TO_PARSE_CORRECT = "1;quest1;answer1;answer2:1;answer3";
    public static final Question QUESTION_CORRECT = new Question(1L, "quest1",
            List.of(new Answer("answer1"),
                    new Answer("answer2", true),
                    new Answer("answer3"))
    );
    public static final String STRING_TO_PARSE_INCORRECT_ONLY_ONE_ANSWER = "1;quest1;answer1:1";
    public static final String STRING_TO_PARSE_INCORRECT_ID = "1A;quest1;answer1:1;answer2;answer3";
    Converter converter;

    @BeforeEach
    void setUp() {
        converter = new ConverterCSV();
    }

    @DisplayName("should be create correct Question for given text")
    @Test
    void shouldCreateCorrectQuestionForGivenText() {
        Question actualQuest = converter.covertToQuest(STRING_TO_PARSE_CORRECT);
        Assertions.assertThat(actualQuest).isEqualTo(QUESTION_CORRECT);
    }

    @DisplayName("should be throw QuestionDaoException for given incorrect text")
    @Test
    void shouldThrowExceptionWhenGivenIncorrectText() {
        Assertions.assertThatThrownBy(() -> converter.covertToQuest(STRING_TO_PARSE_INCORRECT_ONLY_ONE_ANSWER))
                .isInstanceOf(QuestionDaoException.class);
        Assertions.assertThatThrownBy(() -> converter.covertToQuest(STRING_TO_PARSE_INCORRECT_ID))
                .isInstanceOf(QuestionDaoException.class);
    }
}