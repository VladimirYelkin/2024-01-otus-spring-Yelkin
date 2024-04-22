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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
class JpaBookRepositoryTest {

    @Autowired
    TestEntityManager testEm;

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
    void shouldReturnCorrectBookById(Book expectedBook) {

        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
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
        var author = authorRepository.findById(3L).orElseThrow();
        var genres = genreRepository.findAllById(Set.of(1L, 2L));
        var expectedBook = new Book(null, "Book_Title_NEW", author,
                Set.copyOf(genres));

        var returnedBook = bookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        var expectB = testEm.find(Book.class, returnedBook.getId());
        assertThat(expectB).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @Test
    @Transactional
    @DisplayName("Должен сохранить новую книгиу с новым автором и новым жанром")
    void shouldSaveNewBookWithNewAuthorAndNewGenre() {
        var author = new Author(0, "New Author");
        var genre = new Genre(0, "New GENRE");
        var book = new Book(null, " NEW BOOK", author, Set.of(genre));

        var actualSavedBook = bookRepository.save(book);

        assertThat(actualSavedBook.getId()).isNotNull();
        assertThat(bookRepository.findById(actualSavedBook.getId())).isPresent();
        assertThat(authorRepository.findById(actualSavedBook.getAuthor().getId())).isPresent();
        assertThat(genreRepository.findById(actualSavedBook.getGenres().stream().findFirst().orElseThrow().getId())).isPresent();
     }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                Set.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }



    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(Long.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        Set.copyOf(dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2))
                ))
                .toList();
    }

    private static class DbBooksExpected implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)  {
            var dbAuthors = getDbAuthors();
            var dbGenres = getDbGenres();
            return getDbBooks(dbAuthors, dbGenres).stream().map(Arguments::of);
        }
    }
}