package ru.otus.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.model.Answer;


public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String string) {
        return string.endsWith(":1") ?
                new Answer(string.substring(0, string.length() - ":1".length()), true) :
                new Answer(string);
    }
}
