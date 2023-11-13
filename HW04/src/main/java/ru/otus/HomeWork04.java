package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.otus.config.AppConfig;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class HomeWork04 {
    public static void main(String[] args) {
        SpringApplication.run(HomeWork04.class, args);
    }
}