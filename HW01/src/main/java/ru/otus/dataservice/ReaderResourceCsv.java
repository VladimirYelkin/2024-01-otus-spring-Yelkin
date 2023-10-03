package ru.otus.dataservice;

import ru.otus.exeption.QuestionDaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderResourceCsv implements ReaderResource {
    private final String fileName;

    public ReaderResourceCsv(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> getLines() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName);
            if (inputStream != null) {
                try (var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                     var reader = new BufferedReader(streamReader)) {
                    return reader.lines().collect(Collectors.toList());
                } catch (IOException e) {
                    throw new QuestionDaoException(e);
                }
            } else {
                throw new QuestionDaoException("file not found! " + fileName);
            }
        } catch (SecurityException | NullPointerException e) {
            throw new QuestionDaoException(e);
        }
    }
}
