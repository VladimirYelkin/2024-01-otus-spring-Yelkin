package ru.otus.service;


import ru.otus.model.Student;
import ru.otus.model.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
