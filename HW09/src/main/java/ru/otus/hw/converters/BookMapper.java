package ru.otus.hw.converters;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Book;

public interface BookMapper {
    BookDto toDto(Book book);

    BookUpdateDto toDto(BookDto bookDto);
}
