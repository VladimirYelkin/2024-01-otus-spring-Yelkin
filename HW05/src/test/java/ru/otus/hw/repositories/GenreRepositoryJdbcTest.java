package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@JdbcTest
@Import({GenreRepositoryJdbc.class})
class GenreRepositoryJdbcTest {

    @Autowired
    private GenreRepositoryJdbc genreRepositoryJdbc;

    @DisplayName("должен загружать список все жанры")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualGenres = genreRepositoryJdbc.findAll();
        var expectedGenres = getDbGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список жанров согласно входного списка ids")
    @Test
    void shouldReturnCorrectGenreList() {
        List<Long> idx = List.of(1L, 3L);
        var actualGenres = genreRepositoryJdbc.findAllByIds(idx);
        var expectedGenres = getDbGenres().stream()
                .filter(genre -> idx.contains(genre.getId()))
                .collect(Collectors.toList());

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }


    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}