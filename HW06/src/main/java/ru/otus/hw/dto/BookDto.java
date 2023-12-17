package ru.otus.hw.dto;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.Set;

public record BookDto(Long id, String title, Author author, Set<Genre> genres) {
}
