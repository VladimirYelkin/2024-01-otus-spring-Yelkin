package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookDtoToString(BookDto book) {

        var genresString = book.genres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.id(),
                book.title(),
                authorConverter.authorToString(book.author()),
                genresString);
    }

    public String bookWithOutGanresToString(BookDto book) {
        return "Id: %d, title: %s, author: {%s}".formatted(book.id(), book.title(),
                authorConverter.authorToString(book.author()));
    }

    public String bookWithOutGanresToString(Book book) {
        return "Id: %d, title: %s, author: {%s}".formatted(book.getId(), book.getTitle(),
                authorConverter.authorToString(new AuthorDto(book.getAuthor())));
    }
}
