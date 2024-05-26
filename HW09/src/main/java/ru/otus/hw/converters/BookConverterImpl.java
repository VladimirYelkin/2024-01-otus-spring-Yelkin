package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateViewDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

@Component
@RequiredArgsConstructor
public class BookConverterImpl implements BookConverter {

    @Override
    public BookDto convert(Book book) {
        return new BookDto(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                book.getGenres().stream().map(GenreDto::new).toList());
    }

    @Override
    public BookUpdateViewDto convert(BookDto bookDto) {
        return new BookUpdateViewDto(bookDto.id(),
                bookDto.title(),
                bookDto.author().id(),
                bookDto.genres().stream()
                        .findFirst().map(GenreDto::id).orElseThrow(() -> new EntityNotFoundException("Book not found")));
    }
}

