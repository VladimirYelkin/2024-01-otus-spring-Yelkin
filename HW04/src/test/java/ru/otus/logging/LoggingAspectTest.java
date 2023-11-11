package ru.otus.logging;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import ru.otus.fixtures.Quests;
import ru.otus.model.Question;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    private LoggingAspect loggingAspect;
    @Mock
    private Logger logger;

    Object stubObjectClass;
    @Mock
    private JoinPoint joinPoint;

    @BeforeEach
    void setUp() {
         stubObjectClass = new Object();
         loggingAspect = new LoggingAspect();
         when(joinPoint.getThis()).thenReturn(stubObjectClass);
         when(joinPoint.getTarget()).thenReturn(stubObjectClass);
    }

    @ParameterizedTest
    @ArgumentsSource(CheckNotFault.class)
    @NullSource
    void logAfter(Object questions) {
        assertDoesNotThrow(() -> loggingAspect.logAfter(joinPoint, questions));
    }

    private static class CheckNotFault implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(Collections.EMPTY_LIST),
                    Arguments.of(Quests.getList())
            );
        }
    }
}