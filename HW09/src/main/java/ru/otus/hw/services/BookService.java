package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.dto.BookCreateViewDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateViewDto;

public interface BookService {

    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookCreateViewDto bookCreateViewDto);

    void deleteById(long id);

    void update(BookUpdateViewDto bookUpdateViewDto);
}
