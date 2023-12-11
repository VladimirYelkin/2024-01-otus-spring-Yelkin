package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.Set;

public interface GenreService {
    Set<Genre> findAll();
}
