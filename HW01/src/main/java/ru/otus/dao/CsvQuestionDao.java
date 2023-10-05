package ru.otus.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import ru.otus.config.TestFileNameProvider;
import ru.otus.dao.dto.QuestionDto;
import ru.otus.exeption.QuestionDaoException;
import ru.otus.model.Question;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileName;

    public CsvQuestionDao(TestFileNameProvider testFileNameProvider) {
        this.fileName = testFileNameProvider;
    }
//
//    @Override
//    public List<Question> getAll() {
//        return getQuestionsFrom(getLinesFromFile());
//    }
////
//    public List<Question> getQuestionsFrom(List<String> list) {
//        return list.stream()
//                .map(converter::covertToQuest)
//                .collect(Collectors.toList());
//    }
//
//    public List<String> getLinesFromFile() {
//        try {
//            ClassLoader classLoader = getClass().getClassLoader();
//            InputStream inputStream = classLoader.getResourceAsStream(fileName);
//            if (inputStream != null) {
//                try (var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//                     var reader = new BufferedReader(streamReader)) {
//                    return reader.lines().collect(Collectors.toList());
//                } catch (IOException e) {
//                    throw new QuestionDaoException(e);
//                }
//            } else {
//                throw new QuestionDaoException("file not found! " + fileName);
//            }
//        } catch (SecurityException | NullPointerException e) {
//            throw new QuestionDaoException(e);
//        }
//    }


    @Override
    public List<Question> findAll() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName.getTestFileName());
            try (var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                return new CsvToBeanBuilder<QuestionDto>(streamReader)
                        .withType(QuestionDto.class)
                        .withSeparator(';').build()
                        .stream()
                        .map(QuestionDto::toDomainObject).collect(Collectors.toList());
            } catch (IOException e) {
                throw new QuestionDaoException(e);
            }
        } catch (SecurityException | NullPointerException e) {
            throw new QuestionDaoException(e);
        }
    }
}
