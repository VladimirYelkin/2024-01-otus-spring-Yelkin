package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookDtoToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    //bins aaaaaaaaaaaaa 1 1,6//bins aaaaaaaaaaaaa 1 1,6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, List<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, Set.copyOf(genresIds));
        return bookConverter.bookDtoToString(savedBook);
    }

    //bupd 4 dfasdfasdfasd 3 2,5
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, Collection<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, Set.copyOf(genresIds));
        return bookConverter.bookDtoToString(savedBook);
    }

    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }

}
