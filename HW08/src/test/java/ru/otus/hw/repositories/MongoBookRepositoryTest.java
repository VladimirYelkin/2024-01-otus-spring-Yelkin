package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
@DataMongoTest
class MongoBookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @ArgumentsSource(DbBooksExpected.class)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldReturnCorrectBookById(Book expectedBook) {

        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldReturnCorrectBooksList() {
        var expectedBooks = dbBooks;

        var actualBooks = bookRepository.findAll();

        assertThat(actualBooks.get(0)).usingRecursiveComparison().isEqualTo(expectedBooks.get(0));
        assertThat(actualBooks.get(1)).usingRecursiveComparison().isEqualTo(expectedBooks.get(1));
        assertThat(actualBooks.get(2)).usingRecursiveComparison().isEqualTo(expectedBooks.get(2));
    }

    @DisplayName("должен сохранять новую книгу для автора с id 3, и жанрами 1,2 ")
    @Test
    void shouldSaveNewBook() {
        var author = authorRepository.findById("3").orElseThrow();
        var genres = genreRepository.findAllById(Set.of("1", "2"));
        var expectedBook = new Book(null, "Book_Title_NEW", author, genres);

        var returnedBook = bookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

    }

    @Test
//    @Transactional
    @DisplayName("Должен сохранить новую книгиу с новым автором и новым жанром")
    void shouldSaveNewBookWithNewAuthorAndNewGenre() {
        var author = authorRepository.save(new Author("New Author"));
        var genre = genreRepository.save(new Genre("New GENRE"));
        var book = new Book(null, " NEW BOOK", author, List.of(genre));

        var actualSavedBook = bookRepository.save(book);

        assertThat(actualSavedBook.getId()).isNotNull();
        assertThat(bookRepository.findById(actualSavedBook.getId())).isPresent();
        assertThat(authorRepository.findById(actualSavedBook.getAuthor().getId())).isPresent();
        assertThat(genreRepository.findById(actualSavedBook.getGenres().stream().findFirst().orElseThrow().getId())).isPresent();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book("1", "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
    }


    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(String.valueOf(id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(String.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        List.copyOf(dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2))
                ))
                .toList();
    }

    private static class DbBooksExpected implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            var dbAuthors = getDbAuthors();
            var dbGenres = getDbGenres();
            return getDbBooks(dbAuthors, dbGenres).stream().map(Arguments::of);
        }
    }
}