package ru.otus.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@ChangeLog
public class DatabaseChangelog {

    private Map<String, Author> authorMap;

    private Map<String, Genre> genreMap;

    private List<Book> savedBooks;

    @ChangeSet(order = "001", id = "dropDb", author = "vladimir", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "vladimir")
    public void insertAuthors(AuthorRepository repository) {
        var authors = List.of(
                new Author("1", "Author_1"), new Author("2", "Author_2"), new Author("3", "Author_3"));
        authors = repository.saveAll(authors);
        authorMap = authors.stream().collect(toMap(Author::getFullName, Function.identity()));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "vladimir")
    public void insertGenres(GenreRepository repository) {
        List<Genre> genres = List.of(
                new Genre("1", "Genre_1"), new Genre("2", "Genre_2"), new Genre("3", "Genre_3"),
                new Genre("4", "Genre_4"), new Genre("5", "Genre_5"), new Genre("6", "Genre_6"));
        genres = repository.saveAll(genres);
        genreMap = genres.stream().collect(toMap(Genre::getName, Function.identity()));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "vladimir")
    public void insertBooks(BookRepository repository) {
        List<Genre> genres = new ArrayList<>();
        var genresBook1 = List.of(genreMap.get("Genre_1"), genreMap.get("Genre_2"));
        var genresBook2 = List.of(genreMap.get("Genre_3"), genreMap.get("Genre_4"));
        var genresBook3 = List.of(genreMap.get("Genre_5"), genreMap.get("Genre_6"));
        var books = List.of(
                new Book("1", "BookTitle_1", authorMap.get("Author_1"), genresBook1),
                new Book("2", "BookTitle_2", authorMap.get("Author_2"), genresBook2),
                new Book("3", "BookTitle_3", authorMap.get("Author_3"), genresBook3));
        savedBooks = repository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "vladimir")
    public void insertComments(CommentRepository repository) {
        AtomicInteger counter = new AtomicInteger(1);
        savedBooks.stream()
                .map(savedBook -> new Comment(String.valueOf(counter.getAndIncrement()),"Comment for book id: %s".formatted(savedBook.getId()), savedBook))
                .forEach(comment -> repository.save(comment));
    }

    @ChangeSet(order = "006", id = "createIndexComment", author = "vladimir")
    public void createIndexComment(MongoDatabase db) {
        var commentsCollection = db.getCollection("comments");
        commentsCollection.createIndex(Indexes.ascending("book"));
    }
}
