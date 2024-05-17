package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;


    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, String authorId, List<String> genreIds) {
        var book = new Book(title, getAuthorById(authorId), getGenresByIds(genreIds));;
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(String id, String title, String authorId, List<String> genresIds) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        var book = new Book(id, title, getAuthorById(authorId), getGenresByIds(genresIds));
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }


    private Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private List<Genre> getGenresByIds(List<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new EntityNotFoundException("Genre list is empty");
        }
        var genres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        return genres;
    }
}
