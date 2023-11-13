package ru.otus.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Class AppConfig")
@SpringBootTest
class AppConfigTest {

    @Autowired
    private LocaleConfig localeConfig;

    @Autowired
    private TestFileNameProvider testFileNameProvider;

    @DisplayName(" should return correct name of file if change locale")
    @ParameterizedTest
    @ArgumentsSource(NamesOfFileTestByLocale.class)
    void getTestFileName(String locale, String expectedFilename) {
        localeConfig.setLocale(locale);
        String filename = testFileNameProvider.getTestFileName();

        assertThat(filename).isEqualTo(expectedFilename);
    }

    public static class NamesOfFileTestByLocale implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("ru-RU","questions_ru.csv"),
                    Arguments.of("en-US","questions.csv"),
                    Arguments.of("ja-JP","questions.csv")
            );
        }
    }
}