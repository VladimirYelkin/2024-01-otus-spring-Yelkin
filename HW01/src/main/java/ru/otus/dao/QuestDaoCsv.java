package ru.otus.dao;

import ru.otus.dataservice.ReaderResource;
import ru.otus.exeption.QuestionDaoException;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestDaoCsv implements QuestDao {
    private final ReaderResource dataReaderResource;

    private final String delimiter;

    private final String signOfCorrect;

    public QuestDaoCsv(ReaderResource dataReaderResource, String delimiter, String signOfCorrect) {
        this.dataReaderResource = dataReaderResource;
        this.delimiter = delimiter;
        this.signOfCorrect = signOfCorrect;
    }

    @Override
    public List<Question> getAll() {
        List<Question> result = new ArrayList<>();
        for (var line : dataReaderResource.getLines()) {
            var parts = line.split(delimiter);
            if (parts.length < 4) {
                throw new QuestionDaoException("incorrect data in file:" + line);
            }
            var answers = IntStream.range(2, parts.length).boxed()
                    .map(idx -> parts[idx].endsWith(signOfCorrect) ?
                            new Answer(parts[idx].substring(0, parts[idx].length() - signOfCorrect.length()), true)
                            : new Answer(parts[idx])).collect(Collectors.toList());
            result.add(new Question(Long.valueOf(parts[0]), parts[1], answers));
        }
        return result;
    }
}
