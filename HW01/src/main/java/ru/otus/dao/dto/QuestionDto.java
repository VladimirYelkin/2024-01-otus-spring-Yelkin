package ru.otus.dao.dto;

import com.opencsv.bean.CsvBindAndJoinByPosition;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import java.util.List;

@Data
public class QuestionDto {

    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByPosition(position = 1)
    private String text;

    @CsvBindAndJoinByPosition(position = "2-", converter = AnswerCsvConverter.class, elementType = Answer.class)
    private MultiValuedMap<Integer, Answer> answers;

    public Question toDomainObject() {
        return new Question(id, text, List.copyOf(answers.values()));
    }
}
