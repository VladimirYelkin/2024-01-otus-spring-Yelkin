package ru.otus.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final QuestionService testService;

    @Override
    public void run() {
        testService.printQuestions();
    }
}
