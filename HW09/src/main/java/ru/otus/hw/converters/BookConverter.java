package ru.otus.hw.converters;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateViewDto;
import ru.otus.hw.models.Book;

public interface BookConverter {
    BookDto convert(Book book);

    BookUpdateViewDto convert(BookDto bookDto);
}
