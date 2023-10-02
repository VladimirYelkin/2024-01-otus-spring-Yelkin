package ru.otus.dataservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderResourceCsv implements ReaderResource {
    private final String fileName;

    public ReaderResourceCsv(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> getLines() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        List<String> result = new ArrayList<>();
        if (inputStream != null) {
            try (var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 var reader = new BufferedReader(streamReader)) {
                result = reader.lines().collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("file not found! " + fileName);
        }
        return result;
    }
}
