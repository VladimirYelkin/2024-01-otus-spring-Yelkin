package ru.otus.hw.repositories;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import(JpaAuthorRepository.class)

class JpaAuthorRepositoryTest {


    @Autowired
    private AuthorRepository authorRepositoryJdbc;

    @DisplayName("должен находить автора по id")
    @ParameterizedTest
    @ArgumentsSource(CorrectParamsDBAuthor.class)
    void shouldReturnCorrectAuthorById(Author expectedAuthor) {
        var actualAuthor = authorRepositoryJdbc.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех aвторов")
    @Test
    @RepeatedTest(value = 10)
    void shouldReturnCorrectAuthorList() {
        var actualAuthors = authorRepositoryJdbc.findAll();
        var expectedAuthors = getDbAuthors();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    private static class CorrectParamsDBAuthor implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return getDbAuthors().stream()
                    .map(author -> Arguments.of(author));
        }
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 5).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}