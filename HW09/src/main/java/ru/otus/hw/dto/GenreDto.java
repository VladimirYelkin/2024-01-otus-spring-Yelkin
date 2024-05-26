package ru.otus.hw.dto;

import ru.otus.hw.models.Genre;

public record GenreDto(Long id, String name) {
    public GenreDto(Genre genre) {
        this(genre.getId(), genre.getName());
    }
}
