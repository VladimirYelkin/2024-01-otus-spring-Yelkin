package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @DisplayName("должен загружать список все жанры")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualGenres = jpaGenreRepository.findAll();
        var expectedGenres = getDbGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список жанров согласно входного списка ids")
    @Test
    void shouldReturnCorrectGenreList() {
        List<Long> idx = List.of(1L, 3L);
        var actualGenres = jpaGenreRepository.findAllByIds(idx);
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