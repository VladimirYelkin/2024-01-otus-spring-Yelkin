package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                        new BookDto(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                                book.getGenres().stream().map(GenreDto::new).toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(book -> new BookDto(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                        book.getGenres().stream()
                                .map(GenreDto::new)
                                .toList()))
                .toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Set<Long> genreIds) {
        var book = save(0, title, authorId, genreIds);
        return new BookDto(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                book.getGenres().stream().map(GenreDto::new).toList());
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        return bookRepository.findById(id)
                .map(book -> save(book.getId(), title, authorId, genresIds))
                .map(book -> new BookDto(book.getId(), book.getTitle(), new AuthorDto(book.getAuthor()),
                        book.getGenres().stream().map(GenreDto::new).toList()))
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genreIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        List<Genre> genres = isEmpty(genreIds) ? Collections.emptyList()
                : genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genreIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
