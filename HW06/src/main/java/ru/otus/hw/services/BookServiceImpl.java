package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;


    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id)
                .map(book ->
                        new BookDto(book.getId(), book.getTitle(), book.getAuthor(),
                                book.getGenres().stream().collect(Collectors.toSet())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var ganres = genreRepository.findAll().stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        return bookRepository.findAll().stream()
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(),
                        book.getGenres().stream()
                                .map(genre -> ganres.get(genre.getId())).collect(Collectors.toSet())))
                .toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Collection<Long> genresIds) {
        var book = save(0, title, authorId, genresIds);
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres());
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Collection<Long> genresIds) {
        return bookRepository.findById(id)
                .map(book -> save(book.getId(), title, authorId, (genresIds)))
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres()))
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Collection<Long> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        Set<Genre> genres = isEmpty(genresIds) ? Collections.emptySet()
                : genreRepository.findAllByIds(genresIds);
        if (genres.size() != genresIds.size()) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
