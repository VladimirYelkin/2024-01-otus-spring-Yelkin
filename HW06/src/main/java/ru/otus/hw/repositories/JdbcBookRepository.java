package ru.otus.hw.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(@Qualifier("jpaGenreRepository") GenreRepository genreRepository, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.genreRepository = genreRepository;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        String sqlQuery = """
                SELECT b.id, b.title, b.author_id , a.full_name as author_name, bg.genre_id, g.name as genre_name
                FROM books AS b JOIN authors AS a ON b.author_id = a.id
                LEFT JOIN books_genres AS bg ON b.id = bg.book_id
                LEFT JOIN genres AS g ON g.id = bg.genre_id
                WHERE b.id = :id""";
        return namedParameterJdbcOperations.query(sqlQuery, params, new BookResultSetExtractor());
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        removeGenresRelationsForBookId(id);
        namedParameterJdbcOperations.update("delete from books where id  = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        String sqlQuery = """
                SELECT b.id, b.title, b.author_id , a.full_name as author_name 
                FROM books AS b JOIN authors AS a ON b.author_id = a.id""";
        return namedParameterJdbcOperations.query(sqlQuery, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcOperations.query(
                "select book_id, genre_id from books_genres", new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> genreMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        Map<Long, List<Genre>> relationsMap = relations.stream()
                .collect(Collectors.groupingBy(
                        BookGenreRelation::bookId,
                        Collectors.mapping(bookGenreRel -> genreMap.get(bookGenreRel.genreId), Collectors.toList())));
        booksWithoutGenres
                .forEach(book -> book.setGenres(relationsMap.getOrDefault(book.getId(), Collections.emptyList())));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId()));
        namedParameterJdbcOperations.update(
                "insert into books (title,author_id) values (:title,:authorId)", params, keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        Map<String, Object> params = Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId());

        removeGenresRelationsFor(book);
        namedParameterJdbcOperations.update("UPDATE books SET title = :title, author_id = :authorId where id = :id",
                params);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        List<BookGenreRelation> bookGenreRelations = book.getGenres().stream()
                .map(genre -> new BookGenreRelation(book.getId(), genre.getId()))
                .toList();
        namedParameterJdbcOperations.batchUpdate(
                "insert into books_genres (book_id, genre_id) values (:bookId,:genreId)",
                SqlParameterSourceUtils.createBatch(bookGenreRelations));
    }

    private void removeGenresRelationsFor(Book book) {
        removeGenresRelationsForBookId(book.getId());
    }

    private void removeGenresRelationsForBookId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("delete from books_genres where book_id = :id", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("id"),
                    rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("author_name")),
                    Collections.emptyList()
            );
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(
                    rs.getLong("book_id"),
                    rs.getLong("genre_id")
            );
        }
    }


    @SuppressWarnings("ClassCanBeRecord")
    private static class BookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        public BookResultSetExtractor() {
        }

        @Override
        public Optional<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book bookResult = null;
            while (rs.next()) {
                long id = rs.getLong("id");
                if (Objects.isNull(bookResult)) {
                    String title = rs.getString("title");
                    long authorId = rs.getLong("author_id");
                    String authorName = rs.getString("author_name");
                    bookResult = new Book(id, title, new Author(authorId, authorName), new ArrayList<>());
                }
                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                var genres = bookResult.getGenres();
                genres.add(new Genre(genreId, genreName));
            }
            return Optional.ofNullable(bookResult);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
