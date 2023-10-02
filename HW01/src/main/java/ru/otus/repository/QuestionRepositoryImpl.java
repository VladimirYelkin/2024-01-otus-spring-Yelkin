package ru.otus.repository;

import ru.otus.dao.QuestDao;
import ru.otus.model.Question;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestDao questDao;

    public QuestionRepositoryImpl(QuestDao questDao) {
        this.questDao = questDao;
    }

    @Override
    public List<Question> findAll() {
        return questDao.getAll();
    }
}
