package ru.otus.hw.dto;

import ru.otus.hw.models.Book;

import java.util.List;

public record BookDto(Long id, String title, AuthorDto author, List<GenreDto> genres) {
    public BookDto(Book book) {
        this(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                book.getGenres().stream().map(GenreDto::new).toList());
    }

    public BookDto() {
        this(null, null, null, null);
    }
}
