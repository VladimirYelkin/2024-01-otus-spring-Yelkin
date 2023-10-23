package ru.otus.spring.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import ru.otus.config.TestFileNameProvider;


@Configuration
@PropertySource(value = "classpath:test.properties")
@Primary
public class TestContextConfig implements TestFileNameProvider {

    @Value("${test.fileName}")
    private String testFileName;

    @Override
    @Primary
    public String getTestFileName() {
        return testFileName;
    }
}
