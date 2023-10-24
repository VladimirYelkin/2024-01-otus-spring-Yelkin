package ru.otus.spring.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.service.io.IOService;
import ru.otus.service.localize.LocalizedIOService;

@Configuration
public class TestContextConfig {

    @Bean
    @Primary
    LocalizedIOService LocalizedIOService(){
        return Mockito.mock(LocalizedIOService.class);
    }
}
