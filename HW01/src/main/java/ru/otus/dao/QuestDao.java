package ru.otus.dao;

import ru.otus.model.Question;

import java.util.List;

public interface QuestDao {
    List<Question> getAll();
}
