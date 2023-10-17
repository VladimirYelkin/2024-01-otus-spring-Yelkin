package ru.otus.service;

import ru.otus.model.Question;

import java.util.List;

public interface QuestionService {
     void printQuestions();

     List<Question> findAll();

     public void outQuestion(Question question);

     public boolean checkAnswer (Question question, int idAnswer);

     public int getMaxIndexAnswers(Question question);
}
