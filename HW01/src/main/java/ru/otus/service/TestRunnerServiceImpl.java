package ru.otus.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final QuestionService questionService;

    @Override
    public void run() {
        questionService.printQuestions();
    }
}
