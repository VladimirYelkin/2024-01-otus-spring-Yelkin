package ru.otus.hw.converters;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;

@Component
@RequiredArgsConstructor
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                book.getGenres().stream().map(GenreDto::new).toList());
    }

    @Override
    public BookUpdateDto toDto(BookDto bookDto) {
        return new BookUpdateDto(bookDto.id(),
                bookDto.title(),
                bookDto.author().id(),
                bookDto.genres().stream().map(GenreDto::id).collect(Collectors.toSet()));
    }
}

