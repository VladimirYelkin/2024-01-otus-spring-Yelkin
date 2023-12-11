package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.*;
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
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id)
                .map(book ->
                        new Book(book.getId(), book.getTitle(), book.getAuthor(),
                                book.getGenres().stream().collect(Collectors.toSet())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, long authorId, Collection<Long> genresIds) {
        return save(0, title, authorId, (genresIds));
    }

    @Transactional
    @Override
    public Book update(long id, String title, long authorId, Collection<Long> genresIds) {
        return bookRepository.findById(id)
                .map(book -> save(book.getId(), title, authorId, (genresIds)))
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
