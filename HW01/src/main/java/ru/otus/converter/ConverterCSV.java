package ru.otus.converter;

import ru.otus.exeption.QuestionDaoException;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConverterCSV implements Converter {

    private final int indexOfId = 0;

    private final int indexOfQuestion = 1;

    private final int minNumberOfAnswers = 2;

    private final String delimiter = ";";

    private final String signOfCorrect = ":1";

    @Override
    public Question covertToQuest(String line) {
        var partsOfLine = line.split(delimiter);
        long id;
        if (partsOfLine.length <= indexOfQuestion + minNumberOfAnswers) {
            throw new QuestionDaoException("incorrect data: " + line);
        }

        try {
            id = Long.parseLong(partsOfLine[indexOfId]);
        } catch (NumberFormatException e) {
            throw new QuestionDaoException("incorrect data: " + line, e);
        }

        Function<String, Answer> stringToAnswer = string -> string.endsWith(signOfCorrect) ?
                new Answer(string.substring(0, string.length() - signOfCorrect.length()), true) :
                new Answer(string);
        var answers = Arrays.stream(partsOfLine).skip(indexOfQuestion + 1)
                .map(stringToAnswer)
                .collect(Collectors.toList());
        return new Question(id, partsOfLine[indexOfQuestion], answers);
    }

}
