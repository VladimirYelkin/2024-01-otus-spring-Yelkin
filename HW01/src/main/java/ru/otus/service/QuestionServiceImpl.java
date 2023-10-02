package ru.otus.service;

import ru.otus.repository.QuestionRepository;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    private final IOService ioService;

    public QuestionServiceImpl(QuestionRepository questionRepository, IOService ioService) {
        this.questionRepository = questionRepository;
        this.ioService = ioService;
    }

    @Override
    public void showAllQuestions() {
        questionRepository.findAll()
                .forEach(question -> {
                    ioService.println("%d: %s".formatted(question.id(), question.textOfQuestion()));
                    question.answers().stream()
                            .forEach(answer -> ioService.println(answer.toString()));
                });
    }
}
