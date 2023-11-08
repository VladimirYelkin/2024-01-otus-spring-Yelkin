package ru.otus.model;

public record Answer(String textOfAnswer, boolean correct) {
    public Answer(String textOfAnswer) {
        this(textOfAnswer,false);
    }

    @Override
    public String toString() {
        return "Answer: " +
                "textOfAnswer='" + textOfAnswer + '\'' +
                ", correct=" + correct +
                '}';
    }
}
