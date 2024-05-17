package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с комментариями")
@DataMongoTest
@Import({BookServiceImpl.class})
@DirtiesContext
class MongoCommentRepositoryTest  {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void reInitialDB () {
//        super.mongockBeforeEach();
    }

    @DisplayName(" должен загружать информацию о нужном комментарии по его id")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void shouldFindExpectedCommentById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fullText").is("Comment for book id: %s".formatted(id)));
        var expectedComment = mongoTemplate.findOne(query, Comment.class);

        var actualComment = commentRepository.findById(String.valueOf(id));

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get()).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName(" должен удалять комментарий по id")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldDeleteCommentById(String id) {
        assertThat(commentRepository.findById(id)).isPresent();

        commentRepository.deleteById(id);

        assertThat(commentRepository.findById(id)).isEmpty();
    }

    @DisplayName(" должен удалять комментарии вместе с книгой")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void shouldDeleteCommentsByBookId(String id) {
        assertThat(commentRepository.findByBookId(id)).isNotEmpty();

        bookService.deleteById(id);

        assertThat(commentRepository.findByBookId(id)).isEmpty();
    }

    @DisplayName(" должен сохранять новый комментарий для книги ")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldSaveNewComment(String bookId) {
        var expectedComment = new Comment(null, "Comment_for_book" + bookId, mongoTemplate.findById(bookId, Book.class));

        var returnedComment = commentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
        assertThat(mongoTemplate.findById(returnedComment.getId(),Comment.class))
                .usingRecursiveComparison().isEqualTo(returnedComment);
    }
}