package ru.otus.exeption;

public class QuestionDaoException extends RuntimeException {

    public QuestionDaoException(String message) {
        super(message);
    }

    public QuestionDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionDaoException(Throwable cause) {
        super(cause);
    }
}
