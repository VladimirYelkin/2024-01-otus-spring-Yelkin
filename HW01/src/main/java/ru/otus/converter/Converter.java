package ru.otus.converter;

import ru.otus.model.Question;

public interface Converter {
    Question covertToQuest(String line);
}
