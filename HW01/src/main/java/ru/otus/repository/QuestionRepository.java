package ru.otus.repository;

import ru.otus.model.Question;

import java.util.List;

public interface QuestionRepository {

    List<Question> findAll ();
}
