package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;

public interface BookService {

    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto save(BookCreateDto bookCreateDto);

    void deleteById(long id);

    void update(BookUpdateDto bookUpdateDto);
}
