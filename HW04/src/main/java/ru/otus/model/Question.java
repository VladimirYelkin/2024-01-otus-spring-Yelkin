package ru.otus.model;

import java.util.List;

public record Question(Long id, String textOfQuestion, List<Answer> answers) {
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", textOfQuestion='" + textOfQuestion + '\'' +
                ", answers=" + answers +
                '}';
    }
}
