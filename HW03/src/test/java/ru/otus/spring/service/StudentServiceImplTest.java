package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.HomeWork03;
import ru.otus.model.Student;
import ru.otus.service.StudentService;
import ru.otus.service.localize.LocalizedIOService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Метод сервиса создания студента должен ")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HomeWork03.class, TestContextConfig.class})
class StudentServiceImplTest {

    private static final String FIRST_NAME = "First name";
    private static final String LAST_NAME = "Last name";


    @Autowired
    private StudentService studentService;

    @Autowired()
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