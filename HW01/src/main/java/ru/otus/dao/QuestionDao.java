package ru.otus.dao;

import ru.otus.model.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
