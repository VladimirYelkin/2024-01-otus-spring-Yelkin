package ru.otus.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.context.annotation.Configuration;
import ru.otus.config.TestFileNameProvider;
import ru.otus.dao.dto.QuestionDto;
import ru.otus.exeption.QuestionDaoException;
import ru.otus.model.Question;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileName;

    public CsvQuestionDao(TestFileNameProvider testFileNameProvider) {
        this.fileName = testFileNameProvider;
    }

    @Override
    public List<Question> findAll() {

        try (var inputStream = getClass().getClassLoader().getResourceAsStream(fileName.getTestFileName());
             var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            var csvToBean = new CsvToBeanBuilder<QuestionDto>(streamReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';').build();
            return csvToBean.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (SecurityException | NullPointerException | IOException e) {
            throw new QuestionDaoException(e);
        }

    }
}
