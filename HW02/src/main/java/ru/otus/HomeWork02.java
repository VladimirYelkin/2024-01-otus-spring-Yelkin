package ru.otus;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.TestRunnerService;

@Configuration
@ComponentScan
public class HomeWork02 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(HomeWork02.class);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}