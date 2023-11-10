package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.config.TestFileNameProvider;
import ru.otus.dao.CsvQuestionDao;
import ru.otus.model.Student;
import ru.otus.service.localize.LocalizedIOService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("StudentServiceImpl")
@SpringBootTest (classes = {StudentServiceImpl.class,LocalizedIOService.class})
class StudentServiceImplTest {
    private static final String FIRST_NAME = "First name";
    private static final String LAST_NAME = "Last name";

    @Autowired
    private StudentService studentService;

    @MockBean
    private LocalizedIOService ioService;

    @BeforeEach
    void setUp() {
        given(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).willReturn(FIRST_NAME);
        given(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).willReturn(LAST_NAME);
    }

    @Test
    @DisplayName(" корректно возвращать студента")
    void shouldBeDetermineCurrentStudent() {
        Student expectedStudent  = new Student(FIRST_NAME,LAST_NAME);

        Student actualStudent = studentService.determineCurrentStudent();

        assertThat(actualStudent).isEqualTo(expectedStudent);
    }

}