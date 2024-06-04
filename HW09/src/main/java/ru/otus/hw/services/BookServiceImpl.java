package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;


    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto).orElseThrow(()-> new NotFoundException("Book not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto save(BookCreateDto bookCreateDto) {
        var book = save(0, bookCreateDto.getTitle(), bookCreateDto.getAuthorId(),
                Set.of(bookCreateDto.getGenreId()));
        return bookMapper.toDto(book);
    }


    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(BookUpdateDto dto) {
        long id = dto.getId();
        bookRepository.findById(id)
                .map(book -> save(book.getId(), dto.getTitle(), dto.getAuthorId(), Set.of(dto.getGenreId())))
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    private Book save(long id, String title, long authorId, Set<Long> genreIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
        List<Genre> genres = isEmpty(genreIds) ? Collections.emptyList()
                : genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) {
            throw new NotFoundException("Genres with ids %s not found".formatted(genreIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
