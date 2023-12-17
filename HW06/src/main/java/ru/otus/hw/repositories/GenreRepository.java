package ru.otus.hw.repositories;

import ru.otus.hw.models.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreRepository {
    Set<Genre> findAll();

    Set<Genre> findAllByIds(Collection<Long> ids);
}
