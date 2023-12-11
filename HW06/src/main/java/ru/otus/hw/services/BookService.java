package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, Collection<Long> genresIds);

    BookDto update(long id, String title, long authorId, Collection<Long> genresIds);

    void deleteById(long id);
}
