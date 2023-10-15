package ru.otus.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource(value = "classpath:application.properties")
public class AppConfig implements TestFileNameProvider, TestConfig {

    @Value("${test.rightAnswersCountToPass:5}")
    private int rightAnswersCountToPass;

    @Value("${test.fileName:defaultQuestions.csv}")
    private String testFileName;

    @Override
    public String getTestFileName() {
        return testFileName;
    }
}
