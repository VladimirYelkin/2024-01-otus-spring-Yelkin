package ru.otus.spring.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.service.io.IOService;

@Configuration
public class TestContextConfig {

    @Bean
    @Primary
    IOService ioService(){
        return Mockito.mock(IOService.class);
    }
}
