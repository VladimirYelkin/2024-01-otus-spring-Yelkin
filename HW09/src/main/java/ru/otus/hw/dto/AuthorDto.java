package ru.otus.hw.dto;

import ru.otus.hw.models.Author;

public record AuthorDto(Long id, String fullName) {
    public AuthorDto(Author author) {
        this(author.getId(), author.getFullName());
    }
}
