package ru.otus.service;

import ru.otus.model.Question;

public interface QuestionService {
     void printQuestions();

     public void outQuestion(Question question);

     public boolean checkAnswer (Question question, int idAnswer);

     public int getMaxIndexAnswers(Question question);
}
